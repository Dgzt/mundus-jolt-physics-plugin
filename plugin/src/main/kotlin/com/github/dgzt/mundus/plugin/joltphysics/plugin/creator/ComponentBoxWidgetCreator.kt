package com.github.dgzt.mundus.plugin.joltphysics.plugin.creator

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.github.dgzt.mundus.plugin.joltphysics.runtime.JoltPhysicsPlugin
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.PluginConstants
import com.github.dgzt.mundus.plugin.joltphysics.runtime.model.BodyData
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import com.mbrlabs.mundus.pluginapi.ui.RootWidgetCell
import com.mbrlabs.mundus.pluginapi.ui.WidgetAlign
import jolt.physics.collision.shape.BoxShape

object ComponentBoxWidgetCreator {

    private const val STATIC_BOTTOM_PAD = 3.0f
    private const val SIZE_RIGHT_PAD = 10.0f

    fun addBoxWidgets(component: JoltPhysicsComponent, rootWidget: RootWidget) {
        var boxShape = component.shape as BoxShape
        var halfExtend = boxShape.GetHalfExtent()

        var static = component.isStatic

        var massParentWidgetCell: RootWidgetCell? = null
        var mass = if (static) PluginConstants.STATIC_OBJECT_MASS else component.mass
        rootWidget.addCheckbox("Static", static) {
            boxShape = component.shape as BoxShape
            halfExtend = boxShape.GetHalfExtent()
            static = it

            reCreateBoxBody(component, static, halfExtend.GetX() * 2f, halfExtend.GetY() * 2f, halfExtend.GetZ() * 2f)

            if (static) {
                mass = PluginConstants.STATIC_OBJECT_MASS
                massParentWidgetCell?.rootWidget?.clearWidgets()
            } else {
                mass = PluginConstants.DEFAULT_OBJECT_MASS
                ComponentWidgetCreatorUtils.createMassSpinner(massParentWidgetCell!!.rootWidget, mass) { newMass ->
                    run {
                        boxShape = component.shape as BoxShape
                        halfExtend = boxShape.GetHalfExtent()
                        mass = newMass

                        reCreateBoxBody(component, static, halfExtend.GetX() * 2f, halfExtend.GetY() * 2f, halfExtend.GetZ() * 2f)
                    }
                }
            }
        }.setAlign(WidgetAlign.LEFT).setPad(0.0f, 0.0f, STATIC_BOTTOM_PAD, 0.0f)
        rootWidget.addRow()
        rootWidget.addLabel("Size:").grow().setAlign(WidgetAlign.LEFT)
        rootWidget.addRow()
        rootWidget.addSpinner("Width", 0.1f, Float.MAX_VALUE, halfExtend.GetX() * 2f, 0.1f) {
            boxShape = component.shape as BoxShape
            halfExtend = boxShape.GetHalfExtent()

            reCreateBoxBody(component, static, it, halfExtend.GetY() * 2f, halfExtend.GetZ() * 2f)
        }.grow().setPad(0.0f, SIZE_RIGHT_PAD, 0.0f, 0.0f)
        rootWidget.addSpinner("Height", 0.1f, Float.MAX_VALUE, halfExtend.GetY() * 2f, 0.1f) {
            boxShape = component.shape as BoxShape
            halfExtend = boxShape.GetHalfExtent()

            reCreateBoxBody(component, static, halfExtend.GetX() * 2f, it, halfExtend.GetZ() * 2f)
        }.grow().setPad(0.0f, SIZE_RIGHT_PAD, 0.0f, 0.0f)
        rootWidget.addSpinner("Depth", 0.1f, Float.MAX_VALUE, halfExtend.GetZ() * 2f, 0.1f) {
            boxShape = component.shape as BoxShape
            halfExtend = boxShape.GetHalfExtent()

            reCreateBoxBody(component, static, halfExtend.GetX() * 2f, halfExtend.GetY() * 2f, it)
        }.grow()
        rootWidget.addRow()
        massParentWidgetCell = rootWidget.addEmptyWidget()
        massParentWidgetCell.grow().setAlign(WidgetAlign.LEFT)
        if (!static) {
            ComponentWidgetCreatorUtils.createMassSpinner(massParentWidgetCell.rootWidget, mass) { newMass ->
                run {
                    boxShape = component.shape as BoxShape
                    halfExtend = boxShape.GetHalfExtent()
                    mass = newMass

                    reCreateBoxBody(component, static, halfExtend.GetX() * 2f, halfExtend.GetY() * 2f, halfExtend.GetZ() * 2f)
                }
            }
        }
    }

    private fun reCreateBoxBody(component: JoltPhysicsComponent, static: Boolean, width: Float, height: Float, depth: Float, mass: Float = PluginConstants.STATIC_OBJECT_MASS) {
        val bodyInterface = JoltPhysicsPlugin.getPhysicsSystem().GetBodyInterface()
        val body = component.body

        bodyInterface.RemoveBody(body.GetID())
        bodyInterface.DestroyBody(body.GetID())

        val goPosition = component.gameObject.getPosition(Vector3())
        val goRotation = component.gameObject.getRotation(Quaternion())

        val bodyData: BodyData
        if (static) {
            bodyData = JoltPhysicsPlugin.getBodyManager().createBoxBody(goPosition, width, height, depth, goRotation)
        } else {
            bodyData = JoltPhysicsPlugin.getBodyManager().createBoxBody(goPosition, width, height, depth, goRotation, mass)
        }

        component.body = bodyData.body
        component.shape = bodyData.shape
    }
}