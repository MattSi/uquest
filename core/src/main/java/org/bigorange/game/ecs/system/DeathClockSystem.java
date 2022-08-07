package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.DeathClockComponent;
import org.bigorange.game.ecs.component.RemoveComponent;

public class DeathClockSystem extends IteratingSystem {
    private static final String TAG = DeathClockSystem.class.getSimpleName();
    private final ECSEngine ecsEngine;

    public DeathClockSystem(ECSEngine ecsEngine){
        super(Family.all(DeathClockComponent.class).get());
        this.ecsEngine = ecsEngine;
        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final DeathClockComponent deathClockCmp = ECSEngine.deathClockCmpMapper.get(entity);
        deathClockCmp.tick -= deltaTime;
        if(deathClockCmp.tick < 0f){
            entity.add(ecsEngine.createComponent(RemoveComponent.class));
        }
    }
}
