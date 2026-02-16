package com.github.dgzt.mundus.plugin.joltphysics.plugin.creator

import com.mbrlabs.mundus.pluginapi.ui.Cell
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import com.mbrlabs.mundus.pluginapi.ui.WidgetAlign

object ComponentWidgetCreatorUtils {
    fun createMassSpinner(widget: RootWidget, value: Float, f: (Float) -> Unit): Cell {
        val result = widget.addSpinner("Mass:", 0.1f, Float.MAX_VALUE, value) { f.invoke(it) }
        result.grow().setAlign(WidgetAlign.LEFT)

        return result
    }
}