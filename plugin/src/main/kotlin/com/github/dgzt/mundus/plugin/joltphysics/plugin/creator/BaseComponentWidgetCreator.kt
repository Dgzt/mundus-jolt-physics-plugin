package com.github.dgzt.mundus.plugin.joltphysics.plugin.creator

import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent
import com.mbrlabs.mundus.pluginapi.ui.Cell
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import com.mbrlabs.mundus.pluginapi.ui.WidgetAlign

abstract class BaseComponentWidgetCreator {

    companion object {
        const val STATIC_BOTTOM_PAD = 3.0f
        const val SIZE_RIGHT_PAD = 10.0f
    }

    abstract fun addWidgets(component: JoltPhysicsComponent, rootWidget: RootWidget)

    protected fun createMassSpinner(widget: RootWidget, value: Float, f: (Float) -> Unit): Cell {
        val result = widget.addSpinner("Mass:", 0.1f, Float.MAX_VALUE, value) { f.invoke(it) }
        result.grow().setAlign(WidgetAlign.LEFT)

        return result
    }
}