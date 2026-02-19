package com.github.dgzt.mundus.plugin.joltphysics.plugin.creator

import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.github.dgzt.mundus.plugin.joltphysics.plugin.util.GameObjectUtils
import com.github.dgzt.mundus.plugin.joltphysics.runtime.JoltPhysicsPlugin
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent
import com.github.dgzt.mundus.plugin.joltphysics.runtime.type.ShapeType
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent
import com.mbrlabs.mundus.pluginapi.ui.RootWidget
import com.mbrlabs.mundus.pluginapi.ui.RootWidgetCell

object ComponentWidgetCreator {

    private const val BOX = "Box"
    private const val SPHERE = "Sphere"
    private const val CYLINDER = "Cylinder"

    private val boxWidgetCreator = ComponentBoxWidgetCreator()
    private val sphereWidgetCreator = ComponentSphereWidgetCreator()
    private val cylinderWidgetCreator = ComponentCylinderWidgetCreator()

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
        types.add(SPHERE)
        types.add(CYLINDER)
        rootWidget.addSelectBox(types, getSelectBoxType(component)) {
            innerWidgetCell!!.rootWidget!!.clearWidgets()

            val bodyInterface = JoltPhysicsPlugin.getPhysicsSystem().GetBodyInterface()
            val body = component.body
            bodyInterface.RemoveBody(body.GetID())
            bodyInterface.DestroyBody(body.GetID())

            when (it) {
                BOX -> changeToBox(component, modelComponent, innerWidgetCell!!)
                SPHERE -> changeToSphere(component, modelComponent, innerWidgetCell!!)
                CYLINDER -> changeToCylinder(component, modelComponent, innerWidgetCell!!)
                else -> throw RuntimeException("Unsupported model type!")
            }
        }
        rootWidget.addRow()

        innerWidgetCell = rootWidget.addEmptyWidget()
        innerWidgetCell.grow()

        when (component.shapeType) {
            ShapeType.BOX -> boxWidgetCreator.addWidgets(component, innerWidgetCell.rootWidget)
            ShapeType.SPHERE -> sphereWidgetCreator.addWidgets(component, innerWidgetCell.rootWidget)
            ShapeType.CYLINDER -> cylinderWidgetCreator.addWidgets(component, innerWidgetCell.rootWidget)
            else -> throw RuntimeException("Unsupported model type!")
        }

    }

    private fun getSelectBoxType(component: JoltPhysicsComponent): String {
        return when(component.shapeType) {
            ShapeType.BOX -> BOX
            ShapeType.SPHERE -> SPHERE
            ShapeType.CYLINDER -> CYLINDER
            else -> throw RuntimeException("Unsupported model type!")
        }
    }

    private fun changeToBox(
        component: JoltPhysicsComponent,
        modelComponent: ModelComponent,
        innerWidgetCell: RootWidgetCell
    ) {
        // Create static box geom
        val goScale = modelComponent.gameObject.getScale(Vector3())
        val goPosition = modelComponent.gameObject.getPosition(Vector3())
        val goQuaternion = modelComponent.gameObject.getRotation(Quaternion())
        val boundingBox = modelComponent.orientedBoundingBox.bounds
        val geomWidth = boundingBox.width * goScale.x
        val geomHeight = boundingBox.height * goScale.y
        val geomDepth = boundingBox.depth * goScale.z

        val bodyData = JoltPhysicsPlugin.getBodyManager().createBoxBody(goPosition, geomWidth, geomHeight, geomDepth, goQuaternion)
        component.shapeType = ShapeType.BOX
        component.shape = bodyData.shape
        component.body = bodyData.body

        boxWidgetCreator.addWidgets(component, innerWidgetCell.rootWidget)
    }

    private fun changeToSphere(
        component: JoltPhysicsComponent,
        modelComponent: ModelComponent,
        innerWidgetCell: RootWidgetCell
    ) {
        // Create static sphere box geom
        val goScale = modelComponent.gameObject.getScale(Vector3())
        val goPosition = modelComponent.gameObject.getPosition(Vector3())
        val goRotation = modelComponent.gameObject.getRotation(Quaternion())
        val boundingBox = modelComponent.orientedBoundingBox.bounds
        val geomRadius = Math.max(Math.max(boundingBox.width * goScale.x, boundingBox.depth * goScale.z), boundingBox.height * goScale.y) / 2.0f

        val bodyData = JoltPhysicsPlugin.getBodyManager().createSphereBody(goPosition, geomRadius, goRotation)
        component.shapeType = ShapeType.SPHERE
        component.shape = bodyData.shape
        component.body = bodyData.body

        sphereWidgetCreator.addWidgets(component, innerWidgetCell.rootWidget)
    }

    private fun changeToCylinder(
        component: JoltPhysicsComponent,
        modelComponent: ModelComponent,
        innerWidgetCell: RootWidgetCell
    ) {
        // Create static cylinder geom
        val goScale = modelComponent.gameObject.getScale(Vector3())
        val goPosition = modelComponent.gameObject.getPosition(Vector3())
        val goRotation = modelComponent.gameObject.getRotation(Quaternion())
        val boundingBox = modelComponent.orientedBoundingBox.bounds
        val radius = Math.max(boundingBox.width * goScale.x, boundingBox.depth * goScale.z) / 2.0f
        val height = boundingBox.height * goScale.y

        val bodyData = JoltPhysicsPlugin.getBodyManager().createCylinderBody(goPosition, radius, height, goRotation)
        component.shapeType = ShapeType.CYLINDER
        component.shape = bodyData.shape
        component.body = bodyData.body

        cylinderWidgetCreator.addWidgets(component, innerWidgetCell.rootWidget)
    }


}
