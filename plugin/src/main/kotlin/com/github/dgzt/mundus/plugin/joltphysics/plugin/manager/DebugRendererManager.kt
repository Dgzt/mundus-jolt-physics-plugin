package com.github.dgzt.mundus.plugin.joltphysics.plugin.manager

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.utils.Disposable
import com.github.dgzt.mundus.plugin.joltphysics.runtime.JoltPhysicsPlugin
import jolt.gdx.JoltDebugRenderer
import jolt.gdx.gl.GdxDebugRenderer
import jolt.physics.body.BodyManagerDrawSettings

class DebugRendererManager : Disposable {

    var debugRenderer: JoltDebugRenderer? = null
    var debugSettings: BodyManagerDrawSettings? = null

    fun render(camera: Camera) {
        if (!isDebugRendererInitialized()) {
            debugRenderer = GdxDebugRenderer()
            debugSettings = BodyManagerDrawSettings()
        }

        debugRenderer?.begin(camera)
        debugRenderer?.DrawBodies(JoltPhysicsPlugin.getPhysicsSystem(), debugSettings)
        debugRenderer?.end()
    }

    private fun isDebugRendererInitialized(): Boolean {
        return debugRenderer != null && debugSettings != null
    }

    override fun dispose() {
        debugRenderer?.clear()
        debugRenderer?.dispose()
        debugSettings?.dispose()
    }
}
