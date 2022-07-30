package org.bigorange.game.recycle;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.component.AnimationComponent;

/**
 * 动画时间系统，根据时间来确定某一帧
 */
public class AnimationTimerSystem extends IteratingSystem {

    public AnimationTimerSystem() {
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animationComponent = EntityEngine.aniCmpMapper.get(entity);
        animationComponent.aniTimer += deltaTime;
    }
}
