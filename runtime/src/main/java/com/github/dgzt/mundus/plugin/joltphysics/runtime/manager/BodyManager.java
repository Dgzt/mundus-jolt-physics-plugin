package com.github.dgzt.mundus.plugin.joltphysics.runtime.manager;

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
import jolt.physics.collision.shape.MeshShapeSettings;

public class BodyManager {

    private final BodyInterface bodyInterface;

    public BodyManager(final BodyInterface bodyInterface) {
        this.bodyInterface = bodyInterface;
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
                Triangle triangle2 = new Triangle(c00, c11, c01);
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

        return terrainBody;
    }
}
