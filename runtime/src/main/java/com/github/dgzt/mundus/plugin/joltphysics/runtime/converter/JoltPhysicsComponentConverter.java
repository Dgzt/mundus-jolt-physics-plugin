package com.github.dgzt.mundus.plugin.joltphysics.runtime.converter;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.JoltPhysicsPlugin;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.PluginConstants;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.SaveConstants;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.type.ShapeType;
import com.mbrlabs.mundus.commons.assets.Asset;
import com.mbrlabs.mundus.commons.mapper.CustomComponentConverter;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import jolt.physics.collision.shape.BoxShape;

public class JoltPhysicsComponentConverter implements CustomComponentConverter {

    @Override
    public Component.Type getComponentType() {
        return PluginConstants.COMPONENT_TYPE;
    }

    @Override
    public OrderedMap<String, String> convert(final Component component) {
        if (!(component instanceof JoltPhysicsComponent joltComponent)) {
            return null;
        }

        final var map = new OrderedMap<String, String>();
        map.put(SaveConstants.SHAPE, joltComponent.getShapeType().name());

        if (ShapeType.BOX == joltComponent.getShapeType()) {
            final var boxShape = (BoxShape) joltComponent.getShape();
            final var halfExtend = boxShape.GetHalfExtent();

            map.put(SaveConstants.BOX_WIDTH, String.valueOf(halfExtend.GetX() * 2f));
            map.put(SaveConstants.BOX_HEIGHT, String.valueOf(halfExtend.GetY() * 2f));
            map.put(SaveConstants.BOX_DEPTH, String.valueOf(halfExtend.GetZ() * 2f));
            if (!joltComponent.isStatic()) {
                final double mass = joltComponent.getMass();
                map.put(SaveConstants.BOX_MASS, String.valueOf(mass));
            }
        }

        // TODO sphere
        // TODO cylinder
        // TODO capsule

        return map;
    }

    @Override
    public Component convert(final GameObject gameObject, final OrderedMap<String, String> componentProperties, final ObjectMap<String, Asset> assets) {
        final var componentManager = JoltPhysicsPlugin.getComponentManager();
        final var shapeType = ShapeType.valueOf(componentProperties.get(SaveConstants.SHAPE));

        final JoltPhysicsComponent physicsComponent;
        switch (shapeType) {
            case TERRAIN:
                if (gameObject.findComponentByType(Component.Type.TERRAIN) == null) {
                    physicsComponent = componentManager.createTerrainSystemPhysicsComponent(gameObject);
                } else {
                    physicsComponent = componentManager.createTerrainPhysicsComponent(gameObject);
                }
                break;
            case BOX:
                final var boxWidth = Float.parseFloat(componentProperties.get(SaveConstants.BOX_WIDTH));
                final var boxHeight = Float.parseFloat(componentProperties.get(SaveConstants.BOX_HEIGHT));
                final var boxDepth = Float.parseFloat(componentProperties.get(SaveConstants.BOX_DEPTH));
                if (componentProperties.containsKey(SaveConstants.BOX_MASS)) {
                    final var boxMass = Float.parseFloat(componentProperties.get(SaveConstants.BOX_MASS));
                    physicsComponent = componentManager.createBoxPhysicsComponent(gameObject, boxWidth, boxHeight, boxDepth, boxMass);
                } else {
                    physicsComponent = componentManager.createBoxPhysicsComponent(gameObject, boxWidth, boxHeight, boxDepth);
                }
                break;

            default:
                throw new RuntimeException("Not supported shape type");
        }

        // TODO sphere
        // TODO cylinder
        // TODO capsule

        return physicsComponent;
    }
}
