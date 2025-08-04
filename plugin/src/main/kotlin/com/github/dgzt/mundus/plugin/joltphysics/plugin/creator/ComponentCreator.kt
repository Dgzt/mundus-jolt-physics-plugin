package com.github.dgzt.mundus.plugin.joltphysics.plugin.creator

import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent
import com.mbrlabs.mundus.commons.scene3d.GameObject

object ComponentCreator {

    fun create(gameObject: GameObject) : JoltPhysicsComponent {
        return JoltPhysicsComponent(gameObject, null) // TODO
    }
}
