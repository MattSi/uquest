package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.component.AnimationSimpleComponent;

import static org.bigorange.game.GameConfig.UNIT_SCALE;

/**
 * 动画时间系统，根据时间来确定某一帧
 */
public class AnimationSimpleSystem extends IteratingSystem {
    private static final String TAG = AnimationSimpleSystem.class.getSimpleName();

    public AnimationSimpleSystem() {
        super(Family.all(AnimationSimpleComponent.class).get());
        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationSimpleComponent aniCmp = EntityEngine.aniCmpMapper.get(entity);


        aniCmp.aniTimer += deltaTime;
    }
}
