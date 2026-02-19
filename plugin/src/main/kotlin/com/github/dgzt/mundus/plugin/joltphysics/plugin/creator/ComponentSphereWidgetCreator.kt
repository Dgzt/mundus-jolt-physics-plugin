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
import jolt.physics.collision.shape.SphereShape

class ComponentSphereWidgetCreator : BaseComponentWidgetCreator() {

    override fun addWidgets(component: JoltPhysicsComponent, rootWidget: RootWidget) {
        var sphereShape = component.shape as SphereShape
        var radius = sphereShape.GetRadius()

        var static = component.isStatic

        var massParentWidgetCell: RootWidgetCell? = null
        var mass = if (static) PluginConstants.STATIC_OBJECT_MASS else component.mass
        rootWidget.addCheckbox("Static", static) {
            sphereShape = component.shape as SphereShape
            radius = sphereShape.GetRadius()
            static = it

            if (static) {
                mass = PluginConstants.STATIC_OBJECT_MASS
                massParentWidgetCell?.rootWidget?.clearWidgets()

                reCreateSphereBody(component, static, radius)
            } else {
                mass = PluginConstants.DEFAULT_OBJECT_MASS
                createMassSpinner(massParentWidgetCell!!.rootWidget, mass) { newMass ->
                    run {
                        sphereShape = component.shape as SphereShape
                        radius = sphereShape.GetRadius()
                        mass = newMass

                        reCreateSphereBody(component, static, radius, mass)
                    }
                }

                reCreateSphereBody(component, static, radius, mass)
            }
        }.setAlign(WidgetAlign.LEFT).setPad(0.0f, 0.0f, STATIC_BOTTOM_PAD, 0.0f)
        rootWidget.addRow()
        rootWidget.addLabel("Size:").grow().setAlign(WidgetAlign.LEFT)
        rootWidget.addRow()
        rootWidget.addSpinner("Radius", 0.1f, Float.MAX_VALUE, sphereShape.GetRadius(), 0.1f) {
            sphereShape = component.shape as SphereShape
            radius = it

            reCreateSphereBody(component, static, radius, mass)
        }.grow().setPad(0.0f, SIZE_RIGHT_PAD, 0.0f, 0.0f)
        rootWidget.addRow()
        massParentWidgetCell = rootWidget.addEmptyWidget()
        massParentWidgetCell.grow().setAlign(WidgetAlign.LEFT)
        if (!static) {
            createMassSpinner(massParentWidgetCell.rootWidget, mass) { newMass ->
                run {
                    sphereShape = component.shape as SphereShape
                    radius = sphereShape.GetRadius()
                    mass = newMass

                    reCreateSphereBody(component, static, radius, mass)
                }
            }
        }
    }

    private fun reCreateSphereBody(component: JoltPhysicsComponent, static: Boolean, radius: Float, mass: Float = PluginConstants.STATIC_OBJECT_MASS) {
        val bodyInterface = JoltPhysicsPlugin.getPhysicsSystem().GetBodyInterface()
        val body = component.body

        bodyInterface.RemoveBody(body.GetID())
        bodyInterface.DestroyBody(body.GetID())

        val goPosition = component.gameObject.getPosition(Vector3())
        val goRotation = component.gameObject.getRotation(Quaternion())

        val bodyData: BodyData
        if (static) {
            bodyData = JoltPhysicsPlugin.getBodyManager().createSphereBody(goPosition, radius, goRotation)
        } else {
            bodyData = JoltPhysicsPlugin.getBodyManager().createSphereBody(goPosition, radius, goRotation, mass)
        }

        component.body = bodyData.body
        component.shape = bodyData.shape
    }
}