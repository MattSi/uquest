package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.UserMovementComponent;

public class PlayerCameraSystem extends IteratingSystem {
    private static final String TAG = PlayerCameraSystem.class.getSimpleName();
    private final OrthographicCamera gameCamera;

    public PlayerCameraSystem(final OrthographicCamera gameCamera){
        super(Family.all(UserMovementComponent.class).get());

        this.gameCamera = gameCamera;
        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final UserMovementComponent usrMoveCmp = ECSEngine.usrMoveCmpMapper.get(entity);

        gameCamera.position.x += usrMoveCmp.position.x * deltaTime;
        gameCamera.position.y += usrMoveCmp.position.y * deltaTime;
        Gdx.app.debug(TAG, " " + gameCamera.position);
    }
}
