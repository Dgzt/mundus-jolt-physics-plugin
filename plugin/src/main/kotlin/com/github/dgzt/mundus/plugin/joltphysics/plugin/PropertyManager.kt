package com.github.dgzt.mundus.plugin.joltphysics.plugin

import com.github.dgzt.mundus.plugin.joltphysics.plugin.manager.DebugRendererManager

object PropertyManager {
    var joltPhysicsLoaded = false
    var debugRendererEnabled = false

    var debugRendererManager = DebugRendererManager()
}