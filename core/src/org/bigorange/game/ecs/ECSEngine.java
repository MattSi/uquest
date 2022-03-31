package org.bigorange.game.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.bigorange.game.ecs.component.*;
import org.bigorange.game.ecs.system.GameRenderSystem;
import org.bigorange.game.ecs.system.PlayerAnimationSystem;
import org.bigorange.game.ecs.system.PlayerCameraSystem;
import org.bigorange.game.ecs.system.PlayerMovenentSystem;
import org.bigorange.game.map.GameObject;

import static org.bigorange.game.UndergroundQuest.UNIT_SCALE;

public class ECSEngine extends EntityEngine {
    private static final String TAG = ECSEngine.class.getSimpleName();

    public static final ComponentMapper<UserMovementComponent> usrMoveCmpMapper =
            ComponentMapper.getFor(UserMovementComponent.class);

    public static final ComponentMapper<PlayerComponent> playerCmpMapper =
            ComponentMapper.getFor(PlayerComponent.class);


    private final ImmutableArray<Entity> gameObjEntities;

    public ECSEngine(final OrthographicCamera gameCamera){
        gameObjEntities = getEntitiesFor(Family.all(GameObjectComponent.class).get());

        addSystem(new PlayerAnimationSystem());
        addSystem(new PlayerCameraSystem(gameCamera));
        addSystem(new PlayerMovenentSystem());
        addRenderSystem(new GameRenderSystem(this, gameCamera));

    }

    public void createUserMovementCamera(){
        final Entity user = createEntity();
        final UserMovementComponent usrMoveCmp = createComponent(UserMovementComponent.class);
        user.add(usrMoveCmp);

        addEntity(user);
    }

    public void addPlayer(Vector2 spawnLocation) {
        final Entity player = createEntity();
        final Box2DComponent b2dCmp = createComponent(Box2DComponent.class);
        final PlayerComponent playerCmp = createComponent(PlayerComponent.class);
        final AnimationComponent aniCmp = createComponent(AnimationComponent.class);

        b2dCmp.x = spawnLocation.x * UNIT_SCALE;
        b2dCmp.y = spawnLocation.y * UNIT_SCALE;
        b2dCmp.height = 0.5f;
        b2dCmp.width = 0.5f;

        playerCmp.maxSpeed = 2f;

        player.add(b2dCmp);
        player.add(playerCmp);
        player.add(aniCmp);

        addEntity(player);
    }

    public void addGameObject(GameObject gameObj, final Animation<Sprite> animation){
        final Entity gameObjEntity = createEntity();
        final Rectangle boundaries = gameObj.getBoundaries();

        final AnimationComponent aniCmp = createComponent(AnimationComponent.class);
        aniCmp.height = boundaries.height;
        aniCmp.width = boundaries.width;
        aniCmp.animation = animation;
        gameObjEntity.add(aniCmp);

        final GameObjectComponent gameObjCmp = createComponent(GameObjectComponent.class);
        gameObjCmp.id = gameObj.getId();
        gameObjCmp.type = gameObj.getType();
        gameObjEntity.add(gameObjCmp);

        final Box2DComponent b2dCmp = createComponent(Box2DComponent.class);
        b2dCmp.x = gameObj.getBoundaries().x;
        b2dCmp.y = gameObj.getBoundaries().y;
        b2dCmp.width = gameObj.getBoundaries().width;
        b2dCmp.height = gameObj.getBoundaries().height;
        gameObjEntity.add(b2dCmp);

        addEntity(gameObjEntity);
    }

    public ImmutableArray<Entity> getGameObjEntities() {
        return gameObjEntities;
    }
}
