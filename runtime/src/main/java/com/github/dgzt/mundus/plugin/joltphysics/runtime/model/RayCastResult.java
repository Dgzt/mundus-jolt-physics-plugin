package com.github.dgzt.mundus.plugin.joltphysics.runtime.model;

import com.github.dgzt.mundus.plugin.joltphysics.runtime.component.AbstractJoltPhysicsComponent;

public class RayCastResult {

    private AbstractJoltPhysicsComponent component;
    private float contactPointX;
    private float contactPointY;
    private float contactPointZ;


    public AbstractJoltPhysicsComponent getComponent() {
        return component;
    }

    public void setComponent(final AbstractJoltPhysicsComponent component) {
        this.component = component;
    }

    public float getContactPointX() {
        return contactPointX;
    }

    public void setContactPointX(final float contactPointX) {
        this.contactPointX = contactPointX;
    }

    public float getContactPointY() {
        return contactPointY;
    }

    public void setContactPointY(final float contactPointY) {
        this.contactPointY = contactPointY;
    }

    public float getContactPointZ() {
        return contactPointZ;
    }

    public void setContactPointZ(final float contactPointZ) {
        this.contactPointZ = contactPointZ;
    }
}
