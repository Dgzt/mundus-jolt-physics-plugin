package com.github.dgzt.mundus.plugin.joltphysics.runtime.component;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.JoltPhysicsPlugin;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.collector.WaterCollideShapeBodyCollector;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.WaterComponent;
import jolt.JoltTemp;
import jolt.geometry.AABox;
import jolt.math.Vec3;
import jolt.physics.body.BodyID;

public class JoltWaterPhysicsComponent extends AbstractJoltPhysicsComponent implements Disposable {

    private static final Vector3 TMP_VECTOR3 = new Vector3();

    private final WaterCollideShapeBodyCollector collector;
    private final AABox waterBox;

    public JoltWaterPhysicsComponent(final WaterComponent waterComponent) {
        super(waterComponent.gameObject, ShapeType.WATER);

        final Vec3 surfacePoint = JoltTemp.Vec3_1(0f, waterComponent.gameObject.getPosition(TMP_VECTOR3).y, 0f);
        collector = new WaterCollideShapeBodyCollector(surfacePoint, Vec3.sAxisY());

        final Vector3 center = waterComponent.getCenter();
        final Vector3 dimensions = waterComponent.getDimensions();

        final Vec3 waterBoxMin = JoltTemp.Vec3_2(center.x - dimensions.x / 2f, -100f, center.z - dimensions.z / 2f);
        final Vec3 waterBoxMax = JoltTemp.Vec3_3(center.x + dimensions.x / 2f, 0f, center.z + dimensions.z / 2f);
        waterBox = new AABox(waterBoxMin, waterBoxMax);
        waterBox.TranslateVec3(surfacePoint);
    }

    @Override
    public BodyID getBodyID() {
        return null;
    }

    @Override
    public void prePhysicsUpdate(final float delta) {
        collector.setDeltaTime(delta);
        JoltPhysicsPlugin.getPhysicsSystem().GetBroadPhaseQuery().CollideAABox(waterBox, collector);
    }

    @Override
    public void postPhysicsUpdate() {
        // NOOP
    }

    @Override
    public Component clone(GameObject go) {
        // TODO
        return null;
    }

    @Override
    public void dispose() {
        collector.dispose2();
        waterBox.dispose();
    }
}
