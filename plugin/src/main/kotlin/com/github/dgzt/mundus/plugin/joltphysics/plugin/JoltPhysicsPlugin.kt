package com.github.dgzt.mundus.plugin.joltphysics.plugin

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.SharedLibraryLoader
import com.github.dgzt.mundus.plugin.joltphysics.plugin.creator.ComponentCreator
import com.github.dgzt.mundus.plugin.joltphysics.plugin.creator.ComponentWidgetCreator
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.PluginConstants
import com.github.dgzt.mundus.plugin.joltphysics.runtime.converter.JoltPhysicsComponentConverter
import com.mbrlabs.mundus.commons.mapper.CustomComponentConverter
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.pluginapi.ComponentExtension
import com.mbrlabs.mundus.pluginapi.DisposeExtension
import com.mbrlabs.mundus.pluginapi.MenuExtension
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import org.pf4j.Extension
import org.pf4j.Plugin
import com.github.dgzt.mundus.plugin.joltphysics.runtime.JoltPhysicsPlugin as JoltPhysicsRuntimePlugin

class JoltPhysicsPlugin : Plugin() {

    val loader: SharedLibraryLoader = SharedLibraryLoader(System.getProperty("user.home") + "/.mundus/plugins/jolt-physics-plugin.jar")

    override fun start() {
        Gdx.app.debug(PluginConstants.LOG_TAG, "Start Jolt Physics plugin")
        try {
            loader.load("jolt")
            Gdx.app.log(PluginConstants.LOG_TAG, "Jolt Physics native library loaded.")
        } catch (e : Throwable) {
            Gdx.app.error(PluginConstants.LOG_TAG, "Error during load Jolt Physics native library!", e)
        }
    }

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

    @Extension
    class Ode4jDisposeExtension : DisposeExtension {
        override fun dispose() {
            Gdx.app.log(PluginConstants.LOG_TAG, "Dispose")
            if (PropertyManager.joltPhysicsLoaded) {
                JoltPhysicsRuntimePlugin.dispose()
            }
        }
    }
}
