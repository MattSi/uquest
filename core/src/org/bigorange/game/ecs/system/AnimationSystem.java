package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.component.AnimationComponent;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animationComponent = EntityEngine.aniCmpMapper.get(entity);
        animationComponent.aniTimer += deltaTime;
    }
}
