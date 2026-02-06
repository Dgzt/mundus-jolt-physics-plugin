package com.github.dgzt.mundus.plugin.joltphysics.runtime.component;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.JoltPhysicsPlugin;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.model.BodyData;
import com.github.dgzt.mundus.plugin.joltphysics.runtime.type.ShapeType;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import jolt.enums.EMotionType;
import jolt.gdx.JoltGdx;
import jolt.math.Mat44;
import jolt.physics.body.Body;
import jolt.physics.body.BodyID;
import jolt.physics.collision.shape.Shape;

public class JoltPhysicsComponent extends AbstractJoltPhysicsComponent implements Disposable {

    private final Matrix4 tempMatrix4 = new Matrix4();
    private final Vector3 tempPosition = new Vector3();
    private final Quaternion tempQuat = new Quaternion();

    private final ShapeType shapeType;
    // The shape can be null only if game object is terrain and terrain system (parent)
    private Shape shape;
    // The body cana be null only if game object is terrain system (parent)
    private Body body;
    // User-defined data. It can be null
    private Object userData;

    public JoltPhysicsComponent(final GameObject go, final ShapeType shapeType, final BodyData bodyData) {
        this(go, shapeType, bodyData.getShape(), bodyData.getBody());
    }

    public JoltPhysicsComponent(final GameObject go, final ShapeType shapeType, final Shape shape, final Body body) {
        super(go);
        this.shapeType = shapeType;
        this.shape = shape;
        this.body = body;
    }

    @Override
    public BodyID getBodyID() {
        return (body != null) ? body.GetID() : null;
    }

    @Override
    public void prePhysicsUpdate(final float delta) {
        // NOOP
    }

    @Override
    public void postPhysicsUpdate() {
        if (body != null && EMotionType.Dynamic.equals(body.GetMotionType())) {
            final Mat44 mat44 = body.GetWorldTransform();
            tempMatrix4.idt();
            JoltGdx.convert(mat44, tempMatrix4);

            final Vector3 position = tempMatrix4.getTranslation(tempPosition);
            gameObject.setLocalPosition(position.x, position.y, position.z);

            final Quaternion quaternion = tempMatrix4.getRotation(tempQuat);
            gameObject.setLocalRotation(quaternion);
        }
    }

    @Override
    public Component clone(final GameObject go) {
        // TODO
        return null;
    }

    @Override
    public void dispose() {
        JoltPhysicsPlugin.getComponentManager().removeComponent(this);
    }

    public boolean isStatic() {
        return EMotionType.Static.equals(body.GetMotionType());
    }

    public float getMass() {
        final float inverseMass = body.GetMotionProperties().GetInverseMass();
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

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }
}
