package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.component.*;
import org.bigorange.game.gameobjs.AnimationType;

import static org.bigorange.game.GameConfig.UNIT_SCALE;

public class AnimationSystem extends IteratingSystem {

    private final OrthographicCamera camera;

    public AnimationSystem(OrthographicCamera camera) {
        super(Family.all(AnimationComponent2.class, SpeedComponent.class,
                GameObjectComponent2.class).get());
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final SpeedComponent speedCmp = EntityEngine.speedCmpMapper.get(entity);
        final AnimationComponent2 aniCmp = EntityEngine.aniCmpMapper2.get(entity);
        final Box2DComponent b2dCmp = EntityEngine.b2dCmpMapper.get(entity);
        final PlayerComponent playerCmp = EntityEngine.playerCmpMapper.get(entity);

        if (aniCmp.aniType == null) {
            aniCmp.aniType = AnimationType.IDLE;
        }

        if (speedCmp.velocity.equals(Vector2.Zero)) {
            if (playerCmp != null) {
                aniCmp.aniTimer = 0f;
            }
        } else if (speedCmp.velocity.x > 0) {
            aniCmp.aniType = AnimationType.WALK_RIGHT;
        } else if (speedCmp.velocity.x < 0) {
            aniCmp.aniType = AnimationType.WALK_LEFT;
        } else if (speedCmp.velocity.y > 0) {
            aniCmp.aniType = AnimationType.WALK_UP;
        } else if (speedCmp.velocity.y < 0) {
            aniCmp.aniType = AnimationType.WALK_DOWN;
        }

        final AnimationComponent2.AnimationPack<Sprite> aPack = aniCmp.animations.get(aniCmp.aniType);
        aniCmp.currAnimationWidth = aPack.width * UNIT_SCALE * 0.6f;
        aniCmp.currAnimationHeight = aPack.height * UNIT_SCALE * 0.6f;
    }
}
