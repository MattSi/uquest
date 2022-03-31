package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.Box2DComponent;
import org.bigorange.game.ecs.component.PlayerComponent;

public class PlayerCameraSystem extends IteratingSystem {
    private static final String TAG = PlayerCameraSystem.class.getSimpleName();
    private final OrthographicCamera gameCamera;

    public PlayerCameraSystem(final OrthographicCamera gameCamera){
        super(Family.all(PlayerComponent.class, Box2DComponent.class).get());

        this.gameCamera = gameCamera;
        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //final UserMovementComponent usrMoveCmp = ECSEngine.usrMoveCmpMapper.get(entity);
        final Box2DComponent box2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);

        gameCamera.position.x = box2DComponent.x ;
        gameCamera.position.y = box2DComponent.y ;
    }
}
