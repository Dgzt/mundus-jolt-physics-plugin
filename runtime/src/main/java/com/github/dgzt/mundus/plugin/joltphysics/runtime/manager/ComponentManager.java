package com.github.dgzt.mundus.plugin.joltphysics.runtime.manager;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;

public class ComponentManager {

    private static final Vector3 TMP_SCALE = new Vector3();
    private static final Vector3 TMP_POSITION = new Vector3();
    private static final Quaternion TMP_QUATERNION = new Quaternion();

    private final BodyManager bodyManager;

    public ComponentManager(final BodyManager bodyManager) {
        this.bodyManager = bodyManager;
    }

    public JoltPhysicsComponent createTerrainPhysicsComponent(final GameObject gameObject) {
        final var terrainComponent = gameObject.<TerrainComponent>findComponentByType(Component.Type.TERRAIN);
        final var terrainBody = bodyManager.createTerrainBody(terrainComponent);

        return new JoltPhysicsComponent(gameObject, terrainBody);
    }

    public JoltPhysicsComponent createBoxPhysicsComponent(final GameObject gameObject,
                                                          final float mass
    ) {
        final var goScale = gameObject.getScale(TMP_SCALE);
        final var modelComponent = gameObject.<ModelComponent>findComponentByType(Component.Type.MODEL);
        final var bounds = modelComponent.getOrientedBoundingBox().getBounds();
        final var width = bounds.getWidth() * goScale.x;
        final var height = bounds.getHeight() * goScale.y;
        final var depth = bounds.getDepth() * goScale.z;

        return createBoxPhysicsComponent(gameObject, width, height, depth, mass);
    }

    public JoltPhysicsComponent createBoxPhysicsComponent(final GameObject gameObject,
                                                          final float width,
                                                          final float height,
                                                          final float depth,
                                                          final float mass
    ) {
        final var goPosition = gameObject.getPosition(TMP_POSITION);
        final var goQuaternion = gameObject.getRotation(TMP_QUATERNION);

        final var body = bodyManager.createBoxBody(goPosition, width, height, depth, goQuaternion, mass);
        return new JoltPhysicsComponent(gameObject, body);
    }

    public JoltPhysicsComponent createSpherePhysicsComponent(final GameObject gameObject,
                                                             final float radius,
                                                             final float mass
    ) {
        final var goPosition = gameObject.getPosition(TMP_POSITION);
        final var goQuaternion = gameObject.getRotation(TMP_QUATERNION);

        final var body = bodyManager.createSphereBody(goPosition, radius, goQuaternion, mass);
        return new JoltPhysicsComponent(gameObject, body);
    }
}
