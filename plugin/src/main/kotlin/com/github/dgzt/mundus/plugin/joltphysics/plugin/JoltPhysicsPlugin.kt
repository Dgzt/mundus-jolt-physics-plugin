package com.github.dgzt.mundus.plugin.joltphysics.plugin

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

}
