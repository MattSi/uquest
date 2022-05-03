package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.RemoveComponent;
import org.bigorange.game.ecs.component.SteeringComponent;

public class TargetLostSystem extends IteratingSystem {

    // Enemy will lost target if distance over than 6 meters.
    public final static float lostDistance = 6f;

    public TargetLostSystem(){
        super(Family.all(SteeringComponent.class).exclude(RemoveComponent.class).get());
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final SteeringComponent steeringCmp = ECSEngine.steerCmpMapper.get(entity);

        // Check there is a target
        if(steeringCmp.targetLocation == null){
            return;
        }

        Vector2 targetP = steeringCmp.targetLocation.getPosition(),
                localP=steeringCmp.body.getPosition();

        if(localP.dst2(targetP) > lostDistance * lostDistance){
            steeringCmp.steeringBehavior = null;
            steeringCmp.targetLocation = null;
            steeringCmp.body.setLinearVelocity(Vector2.Zero);
            steeringCmp.body.setAngularVelocity(0f);
        }
    }
}
