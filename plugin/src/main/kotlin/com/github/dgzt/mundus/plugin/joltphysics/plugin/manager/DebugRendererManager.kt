package com.github.dgzt.mundus.plugin.joltphysics.plugin.manager

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.utils.Disposable
import com.github.dgzt.mundus.plugin.joltphysics.runtime.JoltPhysicsPlugin
import jolt.gdx.DebugRenderer
import jolt.gdx.GdxModelBatch
import jolt.physics.body.BodyManagerDrawSettings

class DebugRendererManager : Disposable {

    var debugRenderer: DebugRenderer? = null
    var debugSettings: BodyManagerDrawSettings? = null

    fun render(camera: Camera) {
        if (!isDebugRendererInitialized()) {
            debugRenderer = DebugRenderer(GdxModelBatch())
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
        debugRenderer?.dispose()
        debugSettings?.dispose()
    }
}
