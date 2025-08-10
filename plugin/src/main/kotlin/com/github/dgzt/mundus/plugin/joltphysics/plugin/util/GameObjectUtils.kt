package com.github.dgzt.mundus.plugin.joltphysics.plugin.util

import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent
import com.mbrlabs.mundus.commons.scene3d.components.TerrainManagerComponent

object GameObjectUtils {

    fun isTerrainManagerGameObject(gameObject: GameObject): Boolean {
        return gameObject.findComponentByType<TerrainManagerComponent>(Component.Type.TERRAIN_MANAGER) != null
    }

    fun isTerrainGameObject(gameObject: GameObject): Boolean {
        return gameObject.findComponentByType<TerrainComponent>(Component.Type.TERRAIN) != null
    }

    fun isModelGameObject(gameObject: GameObject): Boolean {
        return gameObject.findComponentByType<ModelComponent>(Component.Type.MODEL) != null
    }

    fun hasPhysicsComponent(gameObject: GameObject): Boolean {
        return gameObject.findComponentByType<JoltPhysicsComponent>(Component.Type.PHYSICS) != null
    }

}
