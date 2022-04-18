package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import org.bigorange.game.ecs.component.EnemyComponent;

public class EnemySystem extends IteratingSystem {

    public EnemySystem(){
        super(Family.all(EnemyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
