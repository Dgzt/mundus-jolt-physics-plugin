package com.github.dgzt.mundus.plugin.joltphysics.runtime.converter;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.JoltPhysicsPlugin;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.PluginConstants;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.SaveConstants;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.manager.ComponentManager;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.type.ShapeType;
import com.mbrlabs.mundus.commons.assets.Asset;
import com.mbrlabs.mundus.commons.mapper.CustomComponentConverter;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import jolt.math.Vec3;
import jolt.physics.collision.shape.BoxShape;

public class JoltPhysicsComponentConverter implements CustomComponentConverter {

    @Override
    public Component.Type getComponentType() {
        return PluginConstants.COMPONENT_TYPE;
    }

    @Override
    public OrderedMap<String, String> convert(final Component component) {
        if (!(component instanceof JoltPhysicsComponent)) {
            return null;
        }
        final JoltPhysicsComponent joltComponent = (JoltPhysicsComponent) component;

        final OrderedMap<String, String> map = new OrderedMap<>();
        map.put(SaveConstants.SHAPE, joltComponent.getShapeType().name());

        if (ShapeType.BOX == joltComponent.getShapeType()) {
            final BoxShape boxShape = (BoxShape) joltComponent.getShape();
            final Vec3 halfExtend = boxShape.GetHalfExtent();

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
        final ComponentManager componentManager = JoltPhysicsPlugin.getComponentManager();
        final ShapeType shapeType = ShapeType.valueOf(componentProperties.get(SaveConstants.SHAPE));

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
                final float boxWidth = Float.parseFloat(componentProperties.get(SaveConstants.BOX_WIDTH));
                final float boxHeight = Float.parseFloat(componentProperties.get(SaveConstants.BOX_HEIGHT));
                final float boxDepth = Float.parseFloat(componentProperties.get(SaveConstants.BOX_DEPTH));
                if (componentProperties.containsKey(SaveConstants.BOX_MASS)) {
                    final float boxMass = Float.parseFloat(componentProperties.get(SaveConstants.BOX_MASS));
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
