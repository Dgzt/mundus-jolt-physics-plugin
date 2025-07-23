package com.github.dgzt.mundus.plugin.joltphysics.runtime.component;

import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.AbstractComponent;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import jolt.physics.body.Body;

public class JoltPhysicsComponent extends AbstractComponent {

    private final Body body;

    public JoltPhysicsComponent(final GameObject go, final Body body) {
        super(go);
        this.body = body;
    }

    @Override
    public void update(float delta) {
        // NOOP
    }

    @Override
    public Component clone(final GameObject go) {
        // TODO
        return null;
    }
}
