package com.github.dgzt.mundus.plugin.joltphysics.runtime.manager;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.Layers;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.PluginConstants;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.model.BodyData;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.commons.terrain.Terrain;
import jolt.JoltNew;
import jolt.enums.EActivation;
import jolt.enums.EMotionType;
import jolt.geometry.Triangle;
import jolt.geometry.TriangleList;
import jolt.math.Quat;
import jolt.math.Vec3;
import jolt.physics.body.Body;
import jolt.physics.body.BodyCreationSettings;
import jolt.physics.body.BodyInterface;
import jolt.physics.body.MassProperties;
import jolt.physics.collision.shape.BoxShape;
import jolt.physics.collision.shape.CapsuleShape;
import jolt.physics.collision.shape.CylinderShape;
import jolt.physics.collision.shape.MeshShapeSettings;
import jolt.physics.collision.shape.Shape;
import jolt.physics.collision.shape.SphereShape;

public class BodyManager {

    private static final float TERRAIN_FRICTION = 1.0f;
    private static final Vector3 TMP_VECTOR3 = new Vector3();

    private final float restitution = 0.8f;

    private final BodyInterface bodyInterface;
    private final Vec3 tempVec3;
    private final Quat tempQuat;

    public BodyManager(final BodyInterface bodyInterface) {
        this.bodyInterface = bodyInterface;
        tempVec3 = new Vec3();
        tempQuat = new Quat();
    }

    public Body createTerrainBody(final TerrainComponent terrainComponent) {
        final Vector3 goPosition = terrainComponent.gameObject.getPosition(TMP_VECTOR3);
        final Vec3 inPosition = new Vec3(goPosition.x, goPosition.y, goPosition.z);
        final Terrain terrain = terrainComponent.getTerrainAsset().getTerrain();

        final Vector3 vertexC00 = new Vector3();
        final Vector3 vertexC11 = new Vector3();
        final Vector3 vertexC10 = new Vector3();
        final Vector3 vertexC01 = new Vector3();

        final TriangleList triangles = new TriangleList();
        for (int x = 0; x < terrain.vertexResolution - 1; ++x) {
            for (int z = 0; z < terrain.vertexResolution - 1; ++z) {
                terrain.getVertexPosition(vertexC00, x, z);
                terrain.getVertexPosition(vertexC01, x, z + 1);
                terrain.getVertexPosition(vertexC10, x + 1, z);
                terrain.getVertexPosition(vertexC11, x + 1, z + 1);

                final Vec3 c00 = new Vec3(vertexC00.x, vertexC00.y, vertexC00.z);
                final Vec3 c01 = new Vec3(vertexC01.x, vertexC01.y, vertexC01.z);
                final Vec3 c10 = new Vec3(vertexC10.x, vertexC10.y, vertexC10.z);
                final Vec3 c11 = new Vec3(vertexC11.x, vertexC11.y, vertexC11.z);

                Triangle triangle1 = new Triangle(c00, c11, c10);
                Triangle triangle2 = new Triangle(c00, c01, c11);
                triangles.push_back(triangle1);
                triangles.push_back(triangle2);

                triangle1.dispose();
                triangle2.dispose();
                c00.dispose();
                c01.dispose();
                c10.dispose();
                c11.dispose();
            }

        }

        BodyCreationSettings bodyCreationSettings = JoltNew.BodyCreationSettings(JoltNew.MeshShapeSettings(triangles), inPosition, Quat.sIdentity(), EMotionType.Static, Layers.NON_MOVING);
        Body terrainBody = bodyInterface.CreateBody(bodyCreationSettings);
        bodyInterface.AddBody(terrainBody.GetID(), EActivation.DontActivate);
        triangles.dispose();
        bodyCreationSettings.dispose();
        inPosition.dispose();

        terrainBody.SetFriction(TERRAIN_FRICTION);

        return terrainBody;
    }

    public BodyData createBoxBody(final Vector3 position,
                                  final float width, final float height, final float depth,
                                  final Quaternion quaternion) {
        return createBoxBody(position, width, height, depth, quaternion, PluginConstants.STATIC_OBJECT_MASS);
    }

    public BodyData createBoxBody(final Vector3 position,
                                  final float width, final float height, final float depth,
                                  final Quaternion quaternion,
                                  final float mass) {
        tempVec3.Set(width / 2f, height / 2f, depth / 2f);
        final BoxShape shape = new BoxShape(tempVec3);

        tempVec3.Set(position.x, position.y, position.z);
        tempQuat.Set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);

        final Body body = createBody(shape, mass, tempVec3, tempQuat);
        bodyInterface.AddBody(body.GetID(), EActivation.Activate);
        return new BodyData(body, shape);
    }

    public BodyData createSphereBody(final Vector3 position,
                                     final float radius,
                                     final Quaternion quaternion) {
        return createSphereBody(position, radius, quaternion, PluginConstants.STATIC_OBJECT_MASS);
    }

    public BodyData createSphereBody(final Vector3 position,
                                 final float radius,
                                 final Quaternion quaternion,
                                 final float mass) {
        final SphereShape shape = new SphereShape(radius);

        tempVec3.Set(position.x, position.y, position.z);
        tempQuat.Set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);

        final Body body = createBody(shape, mass, tempVec3, tempQuat);
        bodyInterface.AddBody(body.GetID(), EActivation.Activate);
        return new BodyData(body, shape);
    }

    public BodyData createCylinderBody(final Vector3 position,
                                   final float radius,
                                   final float height,
                                   final Quaternion quaternion) {
        return createCylinderBody(position, radius, height, quaternion, PluginConstants.STATIC_OBJECT_MASS);
    }

    public BodyData createCylinderBody(final Vector3 position,
                                   final float radius,
                                   final float height,
                                   final Quaternion quaternion,
                                   final float mass) {
        final CylinderShape shape = new CylinderShape(height / 2f, radius);

        tempVec3.Set(position.x, position.y, position.z);
        tempQuat.Set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);

        final Body body = createBody(shape, mass, tempVec3, tempQuat);
        bodyInterface.AddBody(body.GetID(), EActivation.Activate);
        return new BodyData(body, shape);
    }

    public BodyData createCapsuleBody(final Vector3 position,
                                  final float radius,
                                  final float height,
                                  final Quaternion quaternion,
                                  final float mass) {
        final CapsuleShape shape = new CapsuleShape(height / 2f, radius);

        tempVec3.Set(position.x, position.y, position.z);
        tempQuat.Set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);

        final Body body = createBody(shape, mass, tempVec3, tempQuat);
        bodyInterface.AddBody(body.GetID(), EActivation.Activate);
        return new BodyData(body, shape);
    }

    private Body createBody(final Shape bodyShape,
                            final float mass,
                            final Vec3 position,
                            final Quat quaternion) {
        final MassProperties massProperties = bodyShape.GetMassProperties();
        EMotionType motionType;
        int layer;
        if (mass > 0f) {
            massProperties.set_mMass(mass);
            motionType = EMotionType.Dynamic;
            layer = Layers.MOVING;
        } else {
            motionType = EMotionType.Static;
            layer = Layers.NON_MOVING;
        }

        final BodyCreationSettings bodySettings = JoltNew.BodyCreationSettings(bodyShape, position, quaternion, motionType, layer);
        bodySettings.set_mMassPropertiesOverride(massProperties);
        bodySettings.set_mRestitution(restitution);
        final Body body = bodyInterface.CreateBody(bodySettings);
        bodySettings.dispose();

        return body;
    }

    public void dispose() {
        tempVec3.dispose();
        tempQuat.dispose();
    }
}
