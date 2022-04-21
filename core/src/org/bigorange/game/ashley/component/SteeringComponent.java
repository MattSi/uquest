package org.bigorange.game.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import org.bigorange.game.ai.steering.Box2DSteeringAdapter;

public class SteeringComponent extends Box2DSteeringAdapter implements Component, Pool.Poolable{

    public SteeringBehavior<Vector2> steeringBehavior;
    public SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());
    public boolean independentFacing;
    public float boundingRadius;


    @Override
    public void reset() {
        super.dispose();
        steeringBehavior = null;
        steeringOutput = null;
        independentFacing = false;
        boundingRadius = 0f;
        steeringOutput.linear.set(Vector2.Zero);
        steeringOutput.angular = 0f;
    }
}
