package org.bigorange.game.ashley.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.bigorange.game.ashley.ECSEngine;
import org.bigorange.game.ashley.component.Box2DComponent;
import org.bigorange.game.ashley.component.RemoveComponent;
import org.bigorange.game.ashley.component.SteeringComponent;

public class SteeringArriveSystem extends IteratingSystem {
    public static final String TAG = SteeringArriveSystem.class.getSimpleName();

    public SteeringArriveSystem() {
        super(Family.all(SteeringComponent.class).exclude(RemoveComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final SteeringComponent steeringCmp = ECSEngine.steerCmpMapper.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);

        if (steeringCmp.steeringBehavior != null) {
            steeringCmp.steeringBehavior.calculateSteering(steeringCmp.steeringOutput);
            //Gdx.app.debug(TAG, "steeringOutput Linear: "+steeringCmp.steeringOutput.linear+ " Angular: "+ steeringCmp.steeringOutput.angular);
            applySteering(steeringCmp, deltaTime);
        }

    }

    private void applySteering(SteeringComponent steeringCmp, float deltaTime) {
        boolean anyAccelerations = false;
        final Body body = steeringCmp.body;
        final SteeringAcceleration<Vector2> steeringOutput = steeringCmp.steeringOutput;


        // Update position and Linear velocity
        if (!steeringOutput.linear.isZero()) {
            // this method internally scales the force by deltaTime
            body.applyForceToCenter(steeringOutput.linear, true);
            anyAccelerations = true;
        }

        // Update orientation and angular velocity
        if (steeringCmp.independentFacing) {
            if (steeringOutput.angular != 0) {
                // this method internally scales the torque deltaTime
                body.applyTorque(steeringOutput.angular, true);
                anyAccelerations = true;
            }
        } else {
            // If we haven't got any velocity, then we can do nothing
            final Vector2 linVel = body.getLinearVelocity();
            if (!linVel.isZero(steeringCmp.getZeroLinearSpeedThreshold())) {
                final float newOrientation = steeringCmp.vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - steeringCmp.getAngularVelocity()) * deltaTime);
                body.setTransform(body.getPosition(), newOrientation);
            }
        }

        if (anyAccelerations) {
            // Cap the linear speed
            final Vector2 velocity = body.getLinearVelocity();
            final float currentSpeedSquare = velocity.len2();
            final float maxLinearSpeed = steeringCmp.getMaxLinearSpeed();
            if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
            }

            // Cap the angular speed
            float maxAngVelocity = steeringCmp.getMaxAngularSpeed();
            if (body.getAngularVelocity() > maxAngVelocity) {
                body.setAngularVelocity(maxAngVelocity);
            }
        }

        Gdx.app.debug(TAG, "LV: " + body.getLinearVelocity() + " AV: " + body.getAngularVelocity() + " Position: " + body.getPosition());
    }
}
