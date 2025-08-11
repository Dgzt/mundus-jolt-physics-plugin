package com.github.dgzt.mundus.plugin.joltphysics.runtime.component;

import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.AbstractComponent;

public abstract class AbstractJoltPhysicsComponent extends AbstractComponent {

    public AbstractJoltPhysicsComponent(final GameObject go) {
        super(go);
        setType(Type.PHYSICS);
    }
}
