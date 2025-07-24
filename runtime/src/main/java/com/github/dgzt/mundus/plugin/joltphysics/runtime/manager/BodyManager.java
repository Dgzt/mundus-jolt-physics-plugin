package com.github.dgzt.mundus.plugin.joltphysics.runtime.manager;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.Layers;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.commons.terrain.Terrain;
import jolt.Jolt;
import jolt.enums.EActivation;
import jolt.enums.EMotionType;
import jolt.geometry.Triangle;
import jolt.geometry.TriangleList;
import jolt.math.Quat;
import jolt.math.Vec3;
import jolt.physics.body.Body;
import jolt.physics.body.BodyCreationSettings;
import jolt.physics.body.BodyInterface;
import jolt.physics.collision.shape.BoxShape;
import jolt.physics.collision.shape.MeshShapeSettings;

public class BodyManager {

    private final float boxRestitution = 0.8f;

    private final BodyInterface bodyInterface;
    private final Vec3 tempVec3;
    private final Quat tempQuat;

    public BodyManager(final BodyInterface bodyInterface) {
        this.bodyInterface = bodyInterface;
        tempVec3 = Jolt.New_Vec3();
        tempQuat = new Quat();
    }

    public Body createTerrainBody(final TerrainComponent terrainComponent) {
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

                final Vec3 c00 = Jolt.New_Vec3(vertexC00.x, vertexC00.y, vertexC00.z);
                final Vec3 c01 = Jolt.New_Vec3(vertexC01.x, vertexC01.y, vertexC01.z);
                final Vec3 c10 = Jolt.New_Vec3(vertexC10.x, vertexC10.y, vertexC10.z);
                final Vec3 c11 = Jolt.New_Vec3(vertexC11.x, vertexC11.y, vertexC11.z);

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

        BodyCreationSettings bodyCreationSettings = Jolt.New_BodyCreationSettings(new MeshShapeSettings(triangles), Vec3.sZero(), Quat.sIdentity(), EMotionType.Static, Layers.NON_MOVING);
        Body terrainBody = bodyInterface.CreateBody(bodyCreationSettings);
        bodyInterface.AddBody(terrainBody.GetID(), EActivation.DontActivate);
        triangles.dispose();
        bodyCreationSettings.dispose();

        terrainBody.SetFriction(1.0f);

        return terrainBody;
    }

    public Body createBoxBody(final Vector3 position,
                              final float width, final float height, final float depth,
                              final Quaternion quaternion,
                              final float mass) {
        tempVec3.Set(width / 2f, height / 2f, depth / 2f);
        final var bodyShape = new BoxShape(tempVec3);

        tempVec3.Set(position.x, position.y, position.z);
        tempQuat.Set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);

        final var massProperties = bodyShape.GetMassProperties();
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

        final var bodySettings = Jolt.New_BodyCreationSettings(bodyShape, tempVec3, tempQuat, motionType, layer);
        bodySettings.set_mMassPropertiesOverride(massProperties);
        bodySettings.set_mRestitution(boxRestitution);
        final var body = bodyInterface.CreateBody(bodySettings);
        bodySettings.dispose();

        bodyInterface.AddBody(body.GetID(), EActivation.Activate);

        return body;
    }

    public void dispose() {
        tempVec3.dispose();
        tempQuat.dispose();
    }
}
