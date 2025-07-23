package com.github.dgzt.mundus.plugin.joltphysics.runtime.manager;

import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;

public class ComponentManager {

    private final BodyManager bodyManager;

    public ComponentManager(final BodyManager bodyManager) {
        this.bodyManager = bodyManager;
    }

    public JoltPhysicsComponent createTerrainPhysicsComponent(final GameObject gameObject) {
        final var terrainComponent = gameObject.<TerrainComponent>findComponentByType(Component.Type.TERRAIN);
        final var terrainBody = bodyManager.createTerrainBody(terrainComponent);

        return new JoltPhysicsComponent(gameObject, terrainBody);
    }
}
