package com.github.dgzt.mundus.plugin.joltphysics.runtime.manager;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.AbstractJoltPhysicsComponent;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.PluginConstants;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.model.BodyData;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import jolt.physics.body.Body;

public class ComponentManager implements Disposable {

    private static final Vector3 TMP_SCALE = new Vector3();
    private static final Vector3 TMP_POSITION = new Vector3();
    private static final Quaternion TMP_QUATERNION = new Quaternion();

    private final BodyManager bodyManager;

    private final Array<AbstractJoltPhysicsComponent> components;

    public ComponentManager(final BodyManager bodyManager) {
        this.bodyManager = bodyManager;
        components = new Array<>();
    }

    public JoltPhysicsComponent createTerrainPhysicsComponent(final GameObject gameObject) {
        final TerrainComponent terrainComponent = gameObject.findComponentByType(Component.Type.TERRAIN);
        final Body terrainBody = bodyManager.createTerrainBody(terrainComponent);

        final JoltPhysicsComponent component = new JoltPhysicsComponent(gameObject, ShapeType.TERRAIN, null, terrainBody);
        addComponent(component);
        return component;
    }

    public JoltPhysicsComponent createTerrainSystemPhysicsComponent(final GameObject gameObject) {
        final JoltPhysicsComponent component = new JoltPhysicsComponent(gameObject, ShapeType.TERRAIN, null, null);
        addComponent(component);
        return component;
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
        final JoltPhysicsComponent component = new JoltPhysicsComponent(gameObject, ShapeType.BOX, bodyData);
        addComponent(component);
        return component;
    }

    public JoltPhysicsComponent createSpherePhysicsComponent(final GameObject gameObject,
                                                             final float radius,
                                                             final float mass
    ) {
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final Quaternion goQuaternion = gameObject.getRotation(TMP_QUATERNION);

        final BodyData bodyData = bodyManager.createSphereBody(goPosition, radius, goQuaternion, mass);
        final JoltPhysicsComponent component = new JoltPhysicsComponent(gameObject, ShapeType.SPHERE, bodyData);
        addComponent(component);
        return component;
    }

    public JoltPhysicsComponent createCylinderPhysicsComponent(final GameObject gameObject,
                                                               final float radius,
                                                               final float height,
                                                               final float mass
    ) {
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final Quaternion goQuaternion = gameObject.getRotation(TMP_QUATERNION);

        final BodyData bodyData = bodyManager.createCylinderBody(goPosition, radius, height, goQuaternion, mass);
        final JoltPhysicsComponent component = new JoltPhysicsComponent(gameObject, ShapeType.CYLINDER, bodyData);
        addComponent(component);
        return component;
    }

    public JoltPhysicsComponent createCapsulePhysicsComponent(final GameObject gameObject,
                                                              final float radius,
                                                              final float height,
                                                              final float mass
    ) {
        final Vector3 goPosition = gameObject.getPosition(TMP_POSITION);
        final Quaternion goQuaternion = gameObject.getRotation(TMP_QUATERNION);

        final BodyData bodyData = bodyManager.createCapsuleBody(goPosition, radius, height, goQuaternion, mass);
        final JoltPhysicsComponent component = new JoltPhysicsComponent(gameObject, ShapeType.CAPSULE, bodyData);
        addComponent(component);
        return component;
    }

    public void addComponent(final AbstractJoltPhysicsComponent component) {
        components.add(component);
    }

    public void removeComponent(final AbstractJoltPhysicsComponent component) {
        components.removeValue(component, true);
    }

    public Array<AbstractJoltPhysicsComponent> getComponents() {
        return components;
    }

    @Override
    public void dispose() {
        components.clear();
    }
}
