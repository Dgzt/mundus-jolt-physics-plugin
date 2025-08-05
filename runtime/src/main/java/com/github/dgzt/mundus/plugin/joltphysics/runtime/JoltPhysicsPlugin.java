package com.github.dgzt.mundus.plugin.joltphysics.runtime;

import com.github.dgzt.mundus.plugin.joltphysics.runtime.config.RuntimeConfig;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.Layers;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.manager.BodyManager;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.manager.ComponentManager;
import jolt.Jolt;
import jolt.JoltLoader;
import jolt.core.Factory;
import jolt.core.JobSystemThreadPool;
import jolt.core.TempAllocatorImpl;
import jolt.physics.PhysicsSystem;
import jolt.physics.collision.ObjectLayerPairFilterTable;
import jolt.physics.collision.broadphase.BroadPhaseLayer;
import jolt.physics.collision.broadphase.BroadPhaseLayerInterfaceTable;
import jolt.physics.collision.broadphase.ObjectVsBroadPhaseLayerFilterTable;

import java.util.concurrent.CompletableFuture;

public class JoltPhysicsPlugin {

    private static JoltPhysicsPlugin INSTANCE = null;

    private PhysicsSystem physicsSystem;
    private Factory factory;
    private final ObjectVsBroadPhaseLayerFilterTable mObjectVsBroadPhaseLayerFilter;
    private final BroadPhaseLayer BP_LAYER_NON_MOVING;
    private final BroadPhaseLayer BP_LAYER_MOVING;
    private final ObjectLayerPairFilterTable mObjectLayerPairFilter;
    private final BroadPhaseLayerInterfaceTable mBroadPhaseLayerInterface;
    private TempAllocatorImpl mTempAllocator;
    private JobSystemThreadPool mJobSystem;

    private final BodyManager bodyManager;
    private final ComponentManager componentManager;

    private final UpdateCallback updateCallback;

    private JoltPhysicsPlugin(final RuntimeConfig config) {
        Jolt.Init();

        int mMaxBodies = 10240;
        int mMaxBodyPairs = 65536;
        int mMaxContactConstraints = 10240;
        int mTempAllocatorSize = 10 * 1024 * 1024;
        int cNumBodyMutexes = 0;

        // Layer that objects can be in, determines which other objects it can collide with
        // Typically you at least want to have 1 layer for moving bodies and 1 layer for static bodies, but you can have more
        // layers if you want. E.g. you could have a layer for high detail collision (which is not used by the physics simulation
        // but only if you do collision testing).

        mObjectLayerPairFilter = new ObjectLayerPairFilterTable(Layers.NUM_LAYERS);
        mObjectLayerPairFilter.EnableCollision(Layers.NON_MOVING, Layers.MOVING);
        mObjectLayerPairFilter.EnableCollision(Layers.MOVING, Layers.MOVING);

        // Each broadphase layer results in a separate bounding volume tree in the broad phase. You at least want to have
        // a layer for non-moving and moving objects to avoid having to update a tree full of static objects every frame.
        // You can have a 1-on-1 mapping between object layers and broadphase layers (like in this case) but if you have
        // many object layers you'll be creating many broad phase trees, which is not efficient.


        int NUM_BROAD_PHASE_LAYERS = 2;
        mBroadPhaseLayerInterface = new BroadPhaseLayerInterfaceTable(Layers.NUM_LAYERS, NUM_BROAD_PHASE_LAYERS);
        BP_LAYER_NON_MOVING = new BroadPhaseLayer((short)0);
        mBroadPhaseLayerInterface.MapObjectToBroadPhaseLayer(Layers.NON_MOVING, BP_LAYER_NON_MOVING);
        BP_LAYER_MOVING = new BroadPhaseLayer((short)1);
        mBroadPhaseLayerInterface.MapObjectToBroadPhaseLayer(Layers.MOVING, BP_LAYER_MOVING);

        mObjectVsBroadPhaseLayerFilter = new ObjectVsBroadPhaseLayerFilterTable(mBroadPhaseLayerInterface, NUM_BROAD_PHASE_LAYERS, mObjectLayerPairFilter, Layers.NUM_LAYERS);


        mTempAllocator = Jolt.New_TempAllocatorImpl(mTempAllocatorSize);
        mJobSystem = Jolt.New_JobSystemThreadPool(4);

        factory = Jolt.New_Factory();
        Factory.set_sInstance(factory);
        Jolt.RegisterTypes();
        physicsSystem = Jolt.New_PhysicsSystem();
        physicsSystem.Init(mMaxBodies, cNumBodyMutexes, mMaxBodyPairs, mMaxContactConstraints, mBroadPhaseLayerInterface, mObjectVsBroadPhaseLayerFilter, mObjectLayerPairFilter);

        bodyManager = new BodyManager(physicsSystem.GetBodyInterface());
        componentManager = new ComponentManager(bodyManager);

        updateCallback = config.updateCallback;
    }

    public static CompletableFuture<InitResult> init() {
        return init(new RuntimeConfig());
    }

    public static CompletableFuture<InitResult> init(final RuntimeConfig config) {
        final var future = new CompletableFuture<InitResult>();

        JoltLoader.init((joltSuccess, exception) -> {
            if (joltSuccess) {
                INSTANCE = new JoltPhysicsPlugin(config);
            }
            future.complete(new InitResult(joltSuccess, exception));
        });

        return future;
    }

    public static PhysicsSystem getPhysicsSystem() {
        return INSTANCE.physicsSystem;
    }

    public static BodyManager getBodyManager() {
        return INSTANCE.bodyManager;
    }

    public static ComponentManager getComponentManager() {
        return INSTANCE.componentManager;
    }

    public static void update() {
        INSTANCE.updateCallback.update(INSTANCE.mTempAllocator, INSTANCE.mJobSystem);
    }

    public static void dispose() {
        INSTANCE.physicsSystem.dispose();
        INSTANCE.BP_LAYER_NON_MOVING.dispose();
        INSTANCE.BP_LAYER_MOVING.dispose();
        INSTANCE.mObjectLayerPairFilter.dispose();

        Factory.set_sInstance(null);
        INSTANCE.factory.dispose();
        Jolt.UnregisterTypes();

        INSTANCE.bodyManager.dispose();
    }
}
