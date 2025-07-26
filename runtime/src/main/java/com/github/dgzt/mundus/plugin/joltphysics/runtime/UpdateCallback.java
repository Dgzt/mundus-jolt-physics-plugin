package com.github.dgzt.mundus.plugin.joltphysics.runtime;

import jolt.core.JobSystemThreadPool;
import jolt.core.TempAllocatorImpl;

public interface UpdateCallback {

    void update(TempAllocatorImpl mTempAllocator, JobSystemThreadPool mJobSystem);
}
