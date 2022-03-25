package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import org.bigorange.game.core.ecs.component.AnimationComponent;
import org.bigorange.game.ecs.component.PlayerComponent;

public class PlayerAnimationSystem extends IteratingSystem {

    public PlayerAnimationSystem() {
        super(Family.all(AnimationComponent.class, PlayerComponent.class).get());


        // create player animations
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
