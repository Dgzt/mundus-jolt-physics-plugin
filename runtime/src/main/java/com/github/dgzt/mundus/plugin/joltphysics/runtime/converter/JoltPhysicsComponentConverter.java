package com.github.dgzt.mundus.plugin.joltphysics.runtime.converter;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.JoltPhysicsComponent;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.constant.PluginConstants;
import com.mbrlabs.mundus.commons.assets.Asset;
import com.mbrlabs.mundus.commons.mapper.CustomComponentConverter;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;

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

        final OrderedMap<String, String> map = new OrderedMap<>();
        // TODO
        return map;
    }

    @Override
    public Component convert(final GameObject gameObject, final OrderedMap<String, String> componentProperties, final ObjectMap<String, Asset> assets) {
        return new JoltPhysicsComponent(gameObject, null, null); //TODO
    }
}
