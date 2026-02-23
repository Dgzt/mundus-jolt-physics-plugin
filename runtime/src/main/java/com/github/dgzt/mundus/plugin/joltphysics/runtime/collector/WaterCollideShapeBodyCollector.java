package com.github.dgzt.mundus.plugin.joltphysics.runtime.collector;

import com.github.dgzt.mundus.plugin.joltphysics.runtime.JoltPhysicsPlugin;
import jolt.JoltNew;
import jolt.core.SharedMutex;
import jolt.math.Vec3;
import jolt.physics.PhysicsSystem;
import jolt.physics.body.Body;
import jolt.physics.body.BodyID;
import jolt.physics.body.BodyLockInterfaceLocking;
import jolt.physics.softbody.CollideShapeBodyCollector;

public class WaterCollideShapeBodyCollector extends CollideShapeBodyCollector {

    private final Vec3 surfacePoint;
    private final Vec3 surfaceNormal;
    private float deltaTime;

    public WaterCollideShapeBodyCollector(final Vec3 surfacePoint, final Vec3 surfaceNormal) {
        this.surfacePoint = JoltNew.Vec3(surfacePoint);
        this.surfaceNormal = JoltNew.Vec3(surfaceNormal);
    }

    @Override
    protected void AddHit(final BodyID inResult) {
        final PhysicsSystem physicsSystem = JoltPhysicsPlugin.getPhysicsSystem();

        final BodyLockInterfaceLocking bodyLockInterfaceLocking = physicsSystem.GetBodyLockInterface();
        final SharedMutex writeLock = bodyLockInterfaceLocking.LockWrite(inResult);
        final Body body = bodyLockInterfaceLocking.TryGetBody(inResult);

        if (body.IsActive()) {
            body.ApplyBuoyancyImpulse(surfacePoint, surfaceNormal, 1.1f, 0.3f, 0.05f, Vec3.sZero(), physicsSystem.GetGravity(), deltaTime);
        }

        bodyLockInterfaceLocking.UnlockWrite(writeLock);
    }

    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }

    public void dispose2() {
        surfacePoint.dispose();
        surfaceNormal.dispose();
    }
}
