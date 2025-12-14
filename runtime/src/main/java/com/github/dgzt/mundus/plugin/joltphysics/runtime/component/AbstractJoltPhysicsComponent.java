package com.github.dgzt.mundus.plugin.joltphysics.runtime.component;

import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.AbstractComponent;
import jolt.physics.body.BodyID;

public abstract class AbstractJoltPhysicsComponent extends AbstractComponent {

    public AbstractJoltPhysicsComponent(final GameObject go) {
        super(go);
        setType(Type.PHYSICS);
    }

    @Override
    public void update(final float delta) {
        // NOOP
    }

    public abstract BodyID getBodyID();

    public abstract void prePhysicsUpdate();

    public abstract void postPhysicsUpdate();
}
