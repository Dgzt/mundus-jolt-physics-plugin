package com.github.dgzt.mundus.plugin.joltphysics.plugin.creator

import com.badlogic.gdx.utils.Array
import com.github.dgzt.mundus.plugin.joltphysics.plugin.util.GameObjectUtils
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent
import com.github.dgzt.mundus.plugin.joltphysics.runtime.type.ShapeType
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import com.mbrlabs.mundus.pluginapi.ui.RootWidgetCell

object ComponentWidgetCreator {

    private const val BOX = "Box"

    fun setup(component: JoltPhysicsComponent, rootWidget: RootWidget) {
        if (GameObjectUtils.isModelGameObject(component.gameObject)) {
            setupModelComponentWidget(component, rootWidget)
        }
    }

    private fun setupModelComponentWidget(component: JoltPhysicsComponent, rootWidget: RootWidget) {
        val modelComponent = component.gameObject.findComponentByType<ModelComponent>(Component.Type.MODEL)
        var innerWidgetCell: RootWidgetCell? = null

        val types = Array<String>()
        types.add(BOX)
        rootWidget.addSelectBox(types, getSelectBoxType(component)) {
            innerWidgetCell!!.rootWidget!!.clearWidgets()

            // TODO destroy
        }
        rootWidget.addRow()

        innerWidgetCell = rootWidget.addEmptyWidget()
        innerWidgetCell.grow()

        when (component.shapeType) {
            ShapeType.BOX -> ComponentBoxWidgetCreator.addBoxWidgets(component, innerWidgetCell.rootWidget)
            else -> throw RuntimeException("Unsupported model type!")
        }

    }

    private fun getSelectBoxType(component: JoltPhysicsComponent): String {
        return when(component.shapeType) {
            ShapeType.BOX -> BOX
            else -> throw RuntimeException("Unsupported model type!")
        }
    }

    }
