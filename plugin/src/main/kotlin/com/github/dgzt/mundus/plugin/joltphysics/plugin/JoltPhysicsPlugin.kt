package com.github.dgzt.mundus.plugin.joltphysics.plugin

import com.github.dgzt.mundus.plugin.joltphysics.plugin.creator.ComponentCreator
import com.github.dgzt.mundus.plugin.joltphysics.plugin.creator.ComponentWidgetCreator
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.PluginConstants
import com.github.dgzt.mundus.plugin.joltphysics.runtime.converter.JoltPhysicsComponentConverter
import com.mbrlabs.mundus.commons.mapper.CustomComponentConverter
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.pluginapi.ComponentExtension
import com.mbrlabs.mundus.pluginapi.MenuExtension
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import org.pf4j.Extension
import org.pf4j.Plugin

class JoltPhysicsPlugin : Plugin() {

    @Extension
    class JoltPhysicsExtension : MenuExtension {

        companion object {
            const val PAD = 5f
        }

        override fun getMenuName(): String = "Jolt Physics"

        override fun setupDialogRootWidget(root: RootWidget) {
            root.addLabel("Jolt Physics").setPad(PAD, PAD, PAD, PAD)
        }
    }

    @Extension
    class Ode4jComponentExtension : ComponentExtension {
        override fun getComponentType(): Component.Type = PluginConstants.COMPONENT_TYPE

        override fun getComponentName(): String = "Jolt Physics"

        override fun createComponent(gameObject: GameObject): Component = ComponentCreator.create(gameObject)

        override fun setupComponentInspectorWidget(component: Component, rootWidget: RootWidget) =
            ComponentWidgetCreator.setup(component as JoltPhysicsComponent, rootWidget)

        override fun getConverter(): CustomComponentConverter = JoltPhysicsComponentConverter()
    }
}
