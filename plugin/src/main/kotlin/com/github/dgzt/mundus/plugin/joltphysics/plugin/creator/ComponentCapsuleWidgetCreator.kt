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
import jolt.physics.collision.shape.CapsuleShape

class ComponentCapsuleWidgetCreator : BaseComponentWidgetCreator() {

    override fun addWidgets(component: JoltPhysicsComponent, rootWidget: RootWidget) {
        var capsuleShape = component.shape as CapsuleShape
        var radius = capsuleShape.GetRadius()
        var height = capsuleShape.GetHalfHeightOfCylinder() * 2f
        var static = component.isStatic

        var massParentWidgetCell: RootWidgetCell? = null
        var mass = if (static) PluginConstants.STATIC_OBJECT_MASS else component.mass
        rootWidget.addCheckbox("Static", static) {
            capsuleShape = component.shape as CapsuleShape
            radius = capsuleShape.GetRadius()
            height = capsuleShape.GetHalfHeightOfCylinder() * 2f
            static = it

            if (static) {
                mass = PluginConstants.STATIC_OBJECT_MASS
                massParentWidgetCell?.rootWidget?.clearWidgets()

                reCreateCapsuleBody(component, static, radius, height)
            } else {
                mass = PluginConstants.DEFAULT_OBJECT_MASS
                createMassSpinner(massParentWidgetCell!!.rootWidget, mass) { newMass ->
                    run {
                        capsuleShape = component.shape as CapsuleShape
                        radius = capsuleShape.GetRadius()
                        height = capsuleShape.GetHalfHeightOfCylinder() * 2f
                        mass = newMass

                        reCreateCapsuleBody(component, static, radius, height, mass)
                    }
                }

                reCreateCapsuleBody(component, static, radius, height, mass)
            }
        }.setAlign(WidgetAlign.LEFT).setPad(0.0f, 0.0f, STATIC_BOTTOM_PAD, 0.0f)
        rootWidget.addRow()
        rootWidget.addLabel("Size:").grow().setAlign(WidgetAlign.LEFT)
        rootWidget.addRow()
        rootWidget.addSpinner("Radius", 0.1f, Float.MAX_VALUE, radius, 0.1f) {
            capsuleShape = component.shape as CapsuleShape
            radius = it
            height = capsuleShape.GetHalfHeightOfCylinder() * 2f

            reCreateCapsuleBody(component, static, radius, height, mass)
        }.grow().setPad(0.0f, SIZE_RIGHT_PAD, 0.0f, 0.0f)
        rootWidget.addSpinner("Height", 0.1f, Float.MAX_VALUE, height, 0.1f) {
            capsuleShape = component.shape as CapsuleShape
            height = it

            reCreateCapsuleBody(component, static, radius, height, mass)
        }.setAlign(WidgetAlign.LEFT)
        rootWidget.addEmptyWidget().grow()
        rootWidget.addRow()
        massParentWidgetCell = rootWidget.addEmptyWidget()
        massParentWidgetCell.grow().setAlign(WidgetAlign.LEFT)
        if (!static) {
            createMassSpinner(massParentWidgetCell.rootWidget, mass) { newMass ->
                run {
                    capsuleShape = component.shape as CapsuleShape
                    radius = capsuleShape.GetRadius()
                    mass = newMass

                    reCreateCapsuleBody(component, static, radius, height, mass)
                }
            }
        }
    }

    private fun reCreateCapsuleBody(component: JoltPhysicsComponent, static: Boolean, radius: Float, height: Float, mass: Float = PluginConstants.STATIC_OBJECT_MASS) {
        val bodyInterface = JoltPhysicsPlugin.getPhysicsSystem().GetBodyInterface()
        val body = component.body

        bodyInterface.RemoveBody(body.GetID())
        bodyInterface.DestroyBody(body.GetID())

        val goPosition = component.gameObject.getPosition(Vector3())
        val goRotation = component.gameObject.getRotation(Quaternion())

        val bodyData: BodyData
        if (static) {
            bodyData = JoltPhysicsPlugin.getBodyManager().createCapsuleBody(goPosition, radius, height, goRotation)
        } else {
            bodyData = JoltPhysicsPlugin.getBodyManager().createCapsuleBody(goPosition, radius, height, goRotation, mass)
        }

        component.body = bodyData.body
        component.shape = bodyData.shape
    }
}
