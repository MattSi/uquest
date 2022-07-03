package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.*;

import static com.badlogic.gdx.math.MathUtils.PI;
import static org.bigorange.game.GameConfig.UNIT_SCALE;

public class EnemyAnimationSystem extends IteratingSystem {

    public EnemyAnimationSystem() {
        super(Family.all(EnemyComponent.class,
                AnimationComponent.class,
                Animation4DirectionsComponent.class,
                Box2DComponent.class,
                SpeedComponent.class
        ).exclude(RemoveComponent.class).get());
    }

    private Array<Sprite> getKeyFrames(final TextureRegion[] textureRegions) {
        final Array<Sprite> keyFrames = new Array<>();

        for (final TextureRegion region : textureRegions) {
            keyFrames.add(new Sprite(region));
        }
        return keyFrames;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final Animation4DirectionsComponent ani4dCmp = ECSEngine.ani4dCmpMapper.get(entity);
        final AnimationComponent aniCmp = ECSEngine.aniCmpMapper.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);
        final SteeringComponent steeringCmp = ECSEngine.steerCmpMapper.get(entity);

        if (aniCmp.animation == null) {
            aniCmp.animation = ani4dCmp.aniDown;
            aniCmp.width = 32 * UNIT_SCALE;
            aniCmp.height = 32 * UNIT_SCALE;
        }

        final Vector2 lv = b2dCmp.body.getLinearVelocity();

        if (lv.epsilonEquals(Vector2.Zero) ) {
            aniCmp.aniTimer = 1f;
            return;
        }

        Vector2 targetP, localP=steeringCmp.body.getPosition();
        if(steeringCmp.targetLocation == null){
            targetP = localP;
        } else {
            targetP = steeringCmp.targetLocation.getPosition();
        }
        float rad = MathUtils.atan2(targetP.y - localP.y, targetP.x - localP.x);
        if (rad < PI * -0.75f) {
            aniCmp.animation = ani4dCmp.aniLeft;
        } else if (rad < PI * -0.25f) {
            aniCmp.animation = ani4dCmp.aniDown;
        } else if (rad < PI * 0.25f) {
            aniCmp.animation = ani4dCmp.aniRight;
        } else if (rad < PI * 0.75f) {
            aniCmp.animation = ani4dCmp.aniUp;
        } else {
            aniCmp.animation = ani4dCmp.aniLeft;
        }


    }
}
