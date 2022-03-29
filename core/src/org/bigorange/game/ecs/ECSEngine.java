package org.bigorange.game.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import org.bigorange.game.ecs.component.AnimationComponent;
import org.bigorange.game.ecs.component.GameObjectComponent;
import org.bigorange.game.ecs.component.UserMovementComponent;
import org.bigorange.game.ecs.system.GameRenderSystem;
import org.bigorange.game.ecs.system.PlayerAnimationSystem;
import org.bigorange.game.ecs.system.PlayerCameraSystem;
import org.bigorange.game.ecs.system.UserMovementSystem;
import org.bigorange.game.map.GameObject;

public class ECSEngine extends EntityEngine {
    private static final String TAG = ECSEngine.class.getSimpleName();

    public static final ComponentMapper<UserMovementComponent> usrMoveCmpMapper =
            ComponentMapper.getFor(UserMovementComponent.class);

    private final ImmutableArray<Entity> gameObjEntities;

    public ECSEngine(final OrthographicCamera gameCamera){
        gameObjEntities = getEntitiesFor(Family.all(GameObjectComponent.class).get());
        addSystem(new UserMovementSystem());
        addSystem(new PlayerCameraSystem(gameCamera));
        addRenderSystem(new GameRenderSystem(this, gameCamera));

    }

    public void createTmpEntity(){
        final Entity user = createEntity();
        final UserMovementComponent usrMoveCmp = createComponent(UserMovementComponent.class);
        user.add(usrMoveCmp);

        addEntity(user);
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

        addEntity(gameObjEntity);
    }

    public ImmutableArray<Entity> getGameObjEntities() {
        return gameObjEntities;
    }
}
