package com.github.dgzt.mundus.plugin.joltphysics.runtime.manager;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.PluginConstants;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.model.BodyData;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import jolt.physics.body.Body;

public class ComponentManager {

    private static final Vector3 TMP_SCALE = new Vector3();
    private static final Vector3 TMP_POSITION = new Vector3();
    private static final Quaternion TMP_QUATERNION = new Quaternion();

    private final BodyManager bodyManager;

    public ComponentManager(final BodyManager bodyManager) {
        this.bodyManager = bodyManager;
    }

    public JoltPhysicsComponent createTerrainPhysicsComponent(final GameObject gameObject) {
        final TerrainComponent terrainComponent = gameObject.<TerrainComponent>findComponentByType(Component.Type.TERRAIN);
        final Body terrainBody = bodyManager.createTerrainBody(terrainComponent);

        return new JoltPhysicsComponent(gameObject, ShapeType.TERRAIN, null, terrainBody);
    }

    public JoltPhysicsComponent createTerrainSystemPhysicsComponent(final GameObject gameObject) {
        return new JoltPhysicsComponent(gameObject, ShapeType.TERRAIN, null, null);
    }

    public JoltPhysicsComponent createBoxPhysicsComponent(final GameObject gameObject) {
        return createBoxPhysicsComponent(gameObject, PluginConstants.STATIC_OBJECT_MASS);
    }

    public JoltPhysicsComponent createBoxPhysicsComponent(final GameObject gameObject,
                                                          final float mass
    ) {
        final Vector3 goScale = gameObject.getScale(TMP_SCALE);
        final ModelComponent modelComponent = gameObject.<ModelComponent>findComponentByType(Component.Type.MODEL);
        final BoundingBox bounds = modelComponent.getOrientedBoundingBox().getBounds();
        final float width = bounds.getWidth() * goScale.x;
        final float height = bounds.getHeight() * goScale.y;
        final float depth = bounds.getDepth() * goScale.z;

        return createBoxPhysicsComponent(gameObject, width, height, depth, mass);
    }

    public JoltPhysicsComponent createBoxPhysicsComponent(final GameObject gameObject,
                                                          final float width,
                                                          final float height,
                                                          final float depth
    ) {
        return createBoxPhysicsComponent(gameObject, width, height, depth, PluginConstants.STATIC_OBJECT_MASS);
    }

    public JoltPhysicsComponent createBoxPhysicsComponent(final GameObject gameObject,
                                                          final float width,
                                                          final float height,
                                                          final float depth,
                                                          final float mass
    ) {
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final Quaternion goQuaternion = gameObject.getRotation(TMP_QUATERNION);

        final BodyData bodyData = bodyManager.createBoxBody(goPosition, width, height, depth, goQuaternion, mass);
        return new JoltPhysicsComponent(gameObject, ShapeType.BOX, bodyData);
    }

    public JoltPhysicsComponent createSpherePhysicsComponent(final GameObject gameObject,
                                                             final float radius,
                                                             final float mass
    ) {
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final Quaternion goQuaternion = gameObject.getRotation(TMP_QUATERNION);

        final BodyData bodyData = bodyManager.createSphereBody(goPosition, radius, goQuaternion, mass);
        return new JoltPhysicsComponent(gameObject, ShapeType.SPHERE, bodyData);
    }

    public JoltPhysicsComponent createCylinderPhysicsComponent(final GameObject gameObject,
                                                               final float radius,
                                                               final float height,
                                                               final float mass
    ) {
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final Quaternion goQuaternion = gameObject.getRotation(TMP_QUATERNION);

        final BodyData bodyData = bodyManager.createCylinderBody(goPosition, radius, height, goQuaternion, mass);
        return new JoltPhysicsComponent(gameObject, ShapeType.CYLINDER, bodyData);
    }

    public JoltPhysicsComponent createCapsulePhysicsComponent(final GameObject gameObject,
                                                              final float radius,
                                                              final float height,
                                                              final float mass
    ) {
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final Quaternion goQuaternion = gameObject.getRotation(TMP_QUATERNION);

        final BodyData bodyData = bodyManager.createCapsuleBody(goPosition, radius, height, goQuaternion, mass);
        return new JoltPhysicsComponent(gameObject, ShapeType.CAPSULE, bodyData);
    }
}
