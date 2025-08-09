package com.github.dgzt.mundus.plugin.joltphysics.runtime.model;

import jolt.physics.body.Body;
import jolt.physics.collision.shape.Shape;

public class BodyData {

    private final Body body;

    private final Shape shape;

    public BodyData(final Body body, final Shape shape) {
        this.body = body;
        this.shape = shape;
    }

    public Body getBody() {
        return body;
    }

    public Shape getShape() {
        return shape;
    }
}
