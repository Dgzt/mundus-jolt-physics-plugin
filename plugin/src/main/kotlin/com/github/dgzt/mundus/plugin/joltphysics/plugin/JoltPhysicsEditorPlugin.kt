package com.github.dgzt.mundus.plugin.joltphysics.plugin

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.github.dgzt.mundus.plugin.joltphysics.plugin.creator.ComponentCreator
import com.github.dgzt.mundus.plugin.joltphysics.plugin.creator.ComponentWidgetCreator
import com.github.dgzt.mundus.plugin.joltphysics.runtime.JoltPhysicsPlugin
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.PluginConstants
import com.github.dgzt.mundus.plugin.joltphysics.runtime.converter.JoltPhysicsComponentConverter
import com.mbrlabs.mundus.commons.mapper.CustomComponentConverter
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.pluginapi.ComponentExtension
import com.mbrlabs.mundus.pluginapi.CustomShaderRenderExtension
import com.mbrlabs.mundus.pluginapi.DisposeExtension
import com.mbrlabs.mundus.pluginapi.MenuExtension
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import org.pf4j.Extension
import org.pf4j.Plugin

class JoltPhysicsEditorPlugin : Plugin() {

    override fun start() {
        Gdx.app.log(PluginConstants.LOG_TAG, "Start Jolt Physics plugin")
        var initResult = JoltPhysicsPlugin.init().get()
        Gdx.app.log(PluginConstants.LOG_TAG, "Jolt Physics loaded: " + initResult.isSuccess)

        PropertyManager.joltPhysicsLoaded = initResult.isSuccess

        if (!initResult.isSuccess) {
            Gdx.app.log(PluginConstants.LOG_TAG, "Error: ", initResult.exception)
        }
    }

    @Extension
    class JoltPhysicsExtension : MenuExtension {

        override fun getMenuName(): String = "Jolt Physics"

        override fun setupDialogRootWidget(root: RootWidget) {
            root.addCheckbox("Debug renderer", PropertyManager.debugRendererEnabled) {
                PropertyManager.debugRendererEnabled = it
            }
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

    @Extension
    class JoltPhysicsCustomShaderRenderExtension : CustomShaderRenderExtension {
        override fun render(camera: Camera) {
            if (PropertyManager.debugRendererEnabled) {
                PropertyManager.debugRendererManager.render(camera)
            }
        }
    }

    @Extension
    class Ode4jDisposeExtension : DisposeExtension {
        override fun dispose() {
            Gdx.app.log(PluginConstants.LOG_TAG, "Dispose")
            PropertyManager.debugRendererManager.dispose()
            if (PropertyManager.joltPhysicsLoaded) {
                JoltPhysicsPlugin.dispose()
            }
        }
    }
}
