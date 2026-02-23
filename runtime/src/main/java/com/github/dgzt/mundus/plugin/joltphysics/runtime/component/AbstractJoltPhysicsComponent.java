package com.github.dgzt.mundus.plugin.joltphysics.runtime.component;

import com.github.dgzt.mundus.plugin.joltphysics.runtime.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.AbstractComponent;
import jolt.physics.body.BodyID;

public abstract class AbstractJoltPhysicsComponent extends AbstractComponent {

    private ShapeType shapeType;

    public AbstractJoltPhysicsComponent(final GameObject go) {
        this(go, ShapeType.CUSTOM);
    }

    public AbstractJoltPhysicsComponent(final GameObject go, final ShapeType shapeType) {
        super(go);
        setShapeType(shapeType);
        setType(Type.PHYSICS);
    }

    @Override
    public void update(final float delta) {
        // NOOP
    }

    public abstract BodyID getBodyID();

    public abstract void prePhysicsUpdate(float delta);

    public abstract void postPhysicsUpdate();

    public ShapeType getShapeType() {
        return shapeType;
    }

    public void setShapeType(final ShapeType shapeType) {
        this.shapeType = shapeType;
    }
}
