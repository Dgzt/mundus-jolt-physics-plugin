package com.github.dgzt.mundus.plugin.joltphysics.runtime.component;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.model.BodyData;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.AbstractComponent;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import jolt.enums.EMotionType;
import jolt.gdx.JoltGdx;
import jolt.physics.body.Body;
import jolt.physics.collision.shape.Shape;

public class JoltPhysicsComponent extends AbstractComponent {

    private final Matrix4 tempMatrix4 = new Matrix4();
    private final Vector3 tempPosition = new Vector3();
    private final Quaternion tempQuat = new Quaternion();

    private ShapeType shapeType;
    private Shape shape;
    private Body body;

    public JoltPhysicsComponent(final GameObject go, final ShapeType shapeType, final BodyData bodyData) {
        this(go, shapeType, bodyData.getShape(), bodyData.getBody());
    }

    public JoltPhysicsComponent(final GameObject go, final ShapeType shapeType, final Shape shape, final Body body) {
        super(go);
        this.shapeType = shapeType;
        this.shape = shape;
        this.body = body;
        setType(Type.PHYSICS);
    }

    @Override
    public void update(final float delta) {
        if (EMotionType.Dynamic.equals(body.GetMotionType())) {
            final var mat44 = body.GetWorldTransform();
            tempMatrix4.idt();
            JoltGdx.mat44_to_matrix4(mat44, tempMatrix4);

            final var position = tempMatrix4.getTranslation(tempPosition);
            gameObject.setLocalPosition(position.x, position.y, position.z);

            final var quaternion = tempMatrix4.getRotation(tempQuat);
            gameObject.setLocalRotation(quaternion);
        }
    }

    @Override
    public Component clone(final GameObject go) {
        // TODO
        return null;
    }

    public boolean isStatic() {
        return EMotionType.Static.equals(body.GetMotionType());
    }

    public float getMass() {
        final var inverseMass = body.GetMotionProperties().GetInverseMass();
        return (inverseMass != 0.0f) ? (1.0f / inverseMass) : 0.0f;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
