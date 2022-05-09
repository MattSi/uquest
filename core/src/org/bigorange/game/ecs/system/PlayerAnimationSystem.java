package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.component.AnimationComponent;
import org.bigorange.game.ecs.component.Box2DComponent;
import org.bigorange.game.ecs.component.PlayerComponent;
import org.bigorange.game.ecs.component.SpeedComponent;
import org.bigorange.game.input.EMouse;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.input.MouseInputListener;
import org.bigorange.game.core.Utils;

import static org.bigorange.game.UndergroundQuest.UNIT_SCALE;

public class PlayerAnimationSystem extends IteratingSystem implements MouseInputListener {
    private final Animation<Sprite> aniLeft;
    private final Animation<Sprite> aniRight;
    private final Animation<Sprite> aniUp;
    private final Animation<Sprite> aniDown;
    private final OrthographicCamera camera;

    private Vector3 mouseMovingTarget;

    public PlayerAnimationSystem(OrthographicCamera camera) {
        super(Family.all(AnimationComponent.class, PlayerComponent.class, Box2DComponent.class).get());
        this.camera = camera;
        mouseMovingTarget = new Vector3(0,0,0);


        // create player animations
        final TextureAtlas.AtlasRegion atlasRegion = Utils.getResourceManager().get("characters/characters.atlas", TextureAtlas.class).findRegion("hero");
        final TextureRegion[][] textureRegions = atlasRegion.split(48, 72);
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
        final PlayerComponent playerCmp = EntityEngine.playerCmpMapper.get(entity);
        final AnimationComponent aniCmp = EntityEngine.aniCmpMapper.get(entity);
        final Box2DComponent b2dCmp = EntityEngine.b2dCmpMapper.get(entity);
        final SpeedComponent speedCmp = EntityEngine.speedCmpMapper.get(entity);

        if (aniCmp.animation == null) {
            aniCmp.animation = aniDown;
            aniCmp.width = 48 * UNIT_SCALE * 0.6f;
            aniCmp.height = 72 * UNIT_SCALE * 0.6f;
        }

        if (speedCmp.velocity.equals(Vector2.Zero) ) {
            aniCmp.aniTimer = 0f;
        } else if (speedCmp.velocity.x > 0) {
            aniCmp.animation = aniRight;
        } else if (speedCmp.velocity.x < 0) {
            aniCmp.animation = aniLeft;
        } else if (speedCmp.velocity.y > 0) {
            aniCmp.animation = aniUp;
        } else if (speedCmp.velocity.y < 0) {
            aniCmp.animation = aniDown;
        }

//        if (playerCmp.speed.equals(Vector2.Zero)) {
//            aniCmp.aniTimer = 0f;
//        } else {
//
//        }
//        final Vector2 position = b2dCmp.body.getPosition();
//        float rad = MathUtils.atan2(mouseMovingTarget.y - position.y, mouseMovingTarget.x - position.x) ;
//
//        if(rad < PI * -0.75f){
//            aniCmp.animation = aniLeft;
//        } else if(rad < PI * -0.25f){
//            aniCmp.animation = aniDown;
//        } else if(rad < PI * 0.25f){
//            aniCmp.animation = aniRight;
//        } else if(rad < PI * 0.75f){
//            aniCmp.animation = aniUp;
//        } else {
//            aniCmp.animation = aniLeft;
//        }

    }

    @Override
    public void mouseMoved(InputManager manager, Vector2 screenPoint) {
        mouseMovingTarget.set(screenPoint.x, screenPoint.y, 0);
        camera.unproject(mouseMovingTarget);
    }

    @Override
    public void buttonDown(InputManager manager, EMouse mouse, Vector2 screenPoint) {

    }

    @Override
    public void buttonUp(InputManager manager, EMouse mouse, Vector2 screenPoint) {

    }


}
