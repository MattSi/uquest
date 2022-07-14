package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.component.AnimationComponent2;
import org.bigorange.game.ecs.component.Box2DComponent;
import org.bigorange.game.ecs.component.GameObjectComponent2;
import org.bigorange.game.ecs.component.SpeedComponent;
import org.bigorange.game.gameobjs.AnimationType;

import static org.bigorange.game.GameConfig.UNIT_SCALE;

public class PlayerAnimationSystem2 extends IteratingSystem {
    private final OrthographicCamera camera;

    public PlayerAnimationSystem2(OrthographicCamera camera) {
        super(Family.all(AnimationComponent2.class, SpeedComponent.class, GameObjectComponent2.class).get());
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final SpeedComponent speedCmp = EntityEngine.speedCmpMapper.get(entity);
        final AnimationComponent2 aniCmp = EntityEngine.aniCmpMapper2.get(entity);
        final Box2DComponent b2dCmp = EntityEngine.b2dCmpMapper.get(entity);

        if (aniCmp.aniType == null) {
            aniCmp.aniType = AnimationType.IDLE;
        }

        if (speedCmp.velocity.equals(Vector2.Zero)) {
            aniCmp.aniTimer = 0f;
        } else if (speedCmp.velocity.x > 0) {
            aniCmp.aniType = AnimationType.WALK_RIGHT;
        } else if (speedCmp.velocity.x < 0) {
            aniCmp.aniType = AnimationType.WALK_LEFT;
        } else if (speedCmp.velocity.y > 0) {
            aniCmp.aniType = AnimationType.WALK_UP;
        } else if (speedCmp.velocity.y < 0) {
            aniCmp.aniType = AnimationType.WALK_DOWN;
        }

        final Sprite keyFrame = aniCmp.animations.get(aniCmp.aniType).getKeyFrames()[0];
        aniCmp.currAnimationWidth = keyFrame.getWidth() * UNIT_SCALE * 1f;
        aniCmp.currAnimationHeight = keyFrame.getHeight() * UNIT_SCALE * 1f;
    }
}
