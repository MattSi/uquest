package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.AnimationComponent;
import org.bigorange.game.ecs.component.Box2DComponent;
import org.bigorange.game.ecs.component.PlayerComponent;
import org.bigorange.game.utils.Utils;

import static org.bigorange.game.UndergroundQuest.UNIT_SCALE;

public class PlayerAnimationSystem extends IteratingSystem {
    private final Animation<Sprite> aniLeft;
    private final Animation<Sprite> aniRight;
    private final Animation<Sprite> aniUp;
    private final Animation<Sprite> aniDown;

    public PlayerAnimationSystem() {
        super(Family.all(AnimationComponent.class, PlayerComponent.class).get());


        // create player animations
        final TextureAtlas.AtlasRegion atlasRegion = Utils.getResourceManager().get("characters/characters.atlas", TextureAtlas.class).findRegion("hero");
        final TextureRegion[][] textureRegions = atlasRegion.split(32, 32);
        aniDown = new Animation<>(0.1f, getKeyFrames(textureRegions[0]), Animation.PlayMode.LOOP);
        aniLeft = new Animation<>(0.1f, getKeyFrames(textureRegions[1]), Animation.PlayMode.LOOP);
        aniRight = new Animation<>(0.1f, getKeyFrames(textureRegions[2]), Animation.PlayMode.LOOP);
        aniUp = new Animation<>(0.1f, getKeyFrames(textureRegions[3]), Animation.PlayMode.LOOP);
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
        final PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(entity);
        final AnimationComponent aniCmp = ECSEngine.aniCmpMapper.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);

        if (aniCmp.animation == null) {
            aniCmp.animation = aniDown;
            aniCmp.width = 32 * UNIT_SCALE;
            aniCmp.height = 32 * UNIT_SCALE;
        }

        if (playerCmp.speed.equals(Vector2.Zero) ) {
            aniCmp.aniTimer = 1f;
        } else if (playerCmp.speed.x > 0) {
            aniCmp.animation = aniRight;
        } else if (playerCmp.speed.x < 0) {
            aniCmp.animation = aniLeft;
        } else if (playerCmp.speed.y > 0) {
            aniCmp.animation = aniUp;
        } else if (playerCmp.speed.y < 0) {
            aniCmp.animation = aniDown;
        }
    }
}
