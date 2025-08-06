package com.github.dgzt.mundus.plugin.joltphysics.plugin.creator

import com.github.dgzt.mundus.plugin.joltphysics.plugin.util.GameObjectUtils
import com.github.dgzt.mundus.plugin.joltphysics.runtime.JoltPhysicsPlugin
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent
import com.mbrlabs.mundus.commons.scene3d.GameObject

object ComponentCreator {

    fun create(gameObject: GameObject) : JoltPhysicsComponent {
        val componentManager = JoltPhysicsPlugin.getComponentManager()
        val physicsComponent: JoltPhysicsComponent

        if (GameObjectUtils.isTerrainManagerGameObject(gameObject)) {
            throw RuntimeException("Implement later")
        } else if (GameObjectUtils.isTerrainGameObject(gameObject)) {
            physicsComponent = componentManager.createTerrainPhysicsComponent(gameObject)
        } else if (GameObjectUtils.isModelGameObject(gameObject)) {
            throw RuntimeException("Implement later")
        } else {
            throw UnsupportedOperationException("Not supported game object type!")

        }

        return physicsComponent
    }
}
