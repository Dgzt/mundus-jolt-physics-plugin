package com.github.dgzt.mundus.plugin.joltphysics.runtime.component;

import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.AbstractComponent;
import com.mbrlabs.mundus.commons.scene3d.components.Component;

public class JoltPhysicsComponent extends AbstractComponent {

    public JoltPhysicsComponent(final GameObject go) {
        super(go);
    }

    @Override
    public void update(float delta) {
        // NOOP
    }

    @Override
    public Component clone(final GameObject go) {
        final JoltPhysicsComponent cloned = new JoltPhysicsComponent(go);
        return cloned;
    }
}
