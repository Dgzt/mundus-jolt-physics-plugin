package com.github.dgzt.mundus.plugin.joltphysics.runtime;

import com.badlogic.gdx.utils.Array;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.AbstractJoltPhysicsComponent;
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

public class JoltPhysicsPlugin {

    private static JoltPhysicsPlugin INSTANCE = null;

    private PhysicsSystem physicsSystem;
    private Factory factory;
    private final ObjectVsBroadPhaseLayerFilterTable objectVsBroadPhaseLayerFilter;
    private final BroadPhaseLayer BP_LAYER_NON_MOVING;
    private final BroadPhaseLayer BP_LAYER_MOVING;
    private final ObjectLayerPairFilterTable objectLayerPairFilter;
    private final BroadPhaseLayerInterfaceTable mBroadPhaseLayerInterface;
    private TempAllocatorImpl tempAllocator;
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

        objectLayerPairFilter = new ObjectLayerPairFilterTable(Layers.NUM_LAYERS);
        objectLayerPairFilter.EnableCollision(Layers.NON_MOVING, Layers.MOVING);
        objectLayerPairFilter.EnableCollision(Layers.MOVING, Layers.MOVING);

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

        objectVsBroadPhaseLayerFilter = new ObjectVsBroadPhaseLayerFilterTable(mBroadPhaseLayerInterface, NUM_BROAD_PHASE_LAYERS, objectLayerPairFilter, Layers.NUM_LAYERS);


        tempAllocator = new TempAllocatorImpl(mTempAllocatorSize);

        int cMaxPhysicsJobs = 2048;
        int cMaxPhysicsBarriers = 8;
        int inNumThreads = -1; // Auto-detect number of threads
        mJobSystem = new JobSystemThreadPool(cMaxPhysicsJobs, cMaxPhysicsBarriers, inNumThreads);

        factory = new Factory();
        Factory.set_sInstance(factory);
        Jolt.RegisterTypes();
        physicsSystem = new PhysicsSystem();
        physicsSystem.Init(mMaxBodies, cNumBodyMutexes, mMaxBodyPairs, mMaxContactConstraints, mBroadPhaseLayerInterface, objectVsBroadPhaseLayerFilter, objectLayerPairFilter);

        bodyManager = new BodyManager(physicsSystem.GetBodyInterface());
        componentManager = new ComponentManager(bodyManager, physicsSystem.GetNarrowPhaseQuery());

        updateCallback = config.updateCallback;
    }

    public static InitResult init() {
        return init(new RuntimeConfig());
    }

    public static InitResult init(final RuntimeConfig config) {
        final InitResult initResult = new InitResult();

        JoltLoader.initSync((joltSuccess, exception) -> {
            initResult.setSuccess(joltSuccess);
            initResult.setException(exception);
        });

        if (initResult.isSuccess()) {
            INSTANCE = new JoltPhysicsPlugin(config);
        }

        return initResult;
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

    public static ObjectVsBroadPhaseLayerFilterTable getObjectVsBroadPhaseLayerFilter() {
        return INSTANCE.objectVsBroadPhaseLayerFilter;
    }

    public static ObjectLayerPairFilterTable getObjectLayerPairFilter() {
        return INSTANCE.objectLayerPairFilter;
    }

    public static TempAllocatorImpl getTempAllocator() {
        return INSTANCE.tempAllocator;
    }

    public static void update() {
        final Array<AbstractJoltPhysicsComponent> components = getComponentManager().getComponents();

        for (int i = 0; i < components.size; ++i) {
            components.get(i).prePhysicsUpdate();
        }

        INSTANCE.updateCallback.update(INSTANCE.tempAllocator, INSTANCE.mJobSystem);

        for (int i = 0; i < components.size; ++i) {
            components.get(i).postPhysicsUpdate();
        }
    }

    public static void clearWorld() {
        Jolt.ClearWorld(INSTANCE.physicsSystem);
    }

    public static void dispose() {
        INSTANCE.physicsSystem.dispose();
        INSTANCE.BP_LAYER_NON_MOVING.dispose();
        INSTANCE.BP_LAYER_MOVING.dispose();
        INSTANCE.objectLayerPairFilter.dispose();

        Factory.set_sInstance(Factory.NULL);
        INSTANCE.factory.dispose();
        Jolt.UnregisterTypes();

        INSTANCE.bodyManager.dispose();
        INSTANCE.componentManager.dispose();
    }
}
