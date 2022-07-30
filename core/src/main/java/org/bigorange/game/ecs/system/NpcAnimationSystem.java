package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class NpcAnimationSystem extends IteratingSystem {

    public NpcAnimationSystem(Family family) {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
