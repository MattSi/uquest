package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.*;
import org.bigorange.game.utils.Utils;

import static org.bigorange.game.UndergroundQuest.UNIT_SCALE;

public class EnemyAnimationSystem extends IteratingSystem {


    public EnemyAnimationSystem(){
        super(Family.all(EnemyComponent.class,
                        AnimationComponent.class,
                        Animation4DirectionsComponent.class,
                        Box2DComponent.class,
                        SpeedComponent.class
                        ).exclude(RemoveComponent.class).get());

        // create player animations
//        final TextureAtlas.AtlasRegion atlasRegion = Utils.getResourceManager().get("characters/characters.atlas",
//                TextureAtlas.class).findRegion(enemyName);
//        final TextureRegion[][] textureRegions = atlasRegion.split(32, 32);
//        aniDown = new Animation<>(0.1f, getKeyFrames(textureRegions[0]), Animation.PlayMode.LOOP);
//        aniLeft = new Animation<>(0.1f, getKeyFrames(textureRegions[1]), Animation.PlayMode.LOOP);
//        aniRight = new Animation<>(0.1f, getKeyFrames(textureRegions[2]), Animation.PlayMode.LOOP);
//        aniUp = new Animation<>(0.1f, getKeyFrames(textureRegions[3]), Animation.PlayMode.LOOP);

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
        final SpeedComponent speedCmp = ECSEngine.speedCmpMapper.get(entity);

        if(aniCmp.animation == null){
            aniCmp.animation = ani4dCmp.aniDown;
            aniCmp.width = 32 * UNIT_SCALE;
            aniCmp.height = 32 * UNIT_SCALE;
        } else if (speedCmp.velocity.x > 0) {
            aniCmp.animation = ani4dCmp.aniRight;
        } else if (speedCmp.velocity.x < 0) {
            aniCmp.animation = ani4dCmp.aniLeft;
        } else if (speedCmp.velocity.y > 0) {
            aniCmp.animation = ani4dCmp.aniUp;
        } else if (speedCmp.velocity.y < 0) {
            aniCmp.animation = ani4dCmp.aniDown;
        }
    }
}
