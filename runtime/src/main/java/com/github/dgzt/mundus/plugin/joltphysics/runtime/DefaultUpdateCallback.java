package com.github.dgzt.mundus.plugin.joltphysics.runtime;

import com.badlogic.gdx.Gdx;
import jolt.core.JobSystemThreadPool;
import jolt.core.TempAllocatorImpl;

public class DefaultUpdateCallback implements UpdateCallback {

    @Override
    public void update(final TempAllocatorImpl mTempAllocator, final JobSystemThreadPool mJobSystem) {
        // Don't go below 30 Hz to prevent spiral of death
        float deltaTime = (float)Math.min(Gdx.graphics.getDeltaTime(), 1.0 / 30.0);
        if(deltaTime > 0) {
            // When running below 55 Hz, do 2 steps instead of 1
            final int numSteps = deltaTime > 1.0 / 55.0 ? 2 : 1;
//            JoltPhysicsPlugin.update(deltaTime, numSteps);
            final var physicsSystem = JoltPhysicsPlugin.getPhysicsSystem();
            physicsSystem.Update(deltaTime, numSteps, mTempAllocator, mJobSystem);
        }
    }
}
