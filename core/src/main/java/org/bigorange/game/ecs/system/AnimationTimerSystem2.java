package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.component.AnimationComponent2;

public class AnimationTimerSystem2 extends IteratingSystem {
    private static final String TAG = AnimationTimerSystem2.class.getSimpleName();

    public AnimationTimerSystem2() {
        super(Family.all(AnimationComponent2.class).get());
        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent2 aniCmp = EntityEngine.aniCmpMapper2.get(entity);
        if (!aniCmp.isEnable) {
            return;
        }

        aniCmp.aniTimer += deltaTime;
    }
}
