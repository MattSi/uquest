package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.component.AnimationComponent;
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
        final Box2DComponent box2DComponent = EntityEngine.b2dCmpMapper.get(entity);

        gameCamera.position.x = box2DComponent.positionBeforeUpdate.x;
        gameCamera.position.y = box2DComponent.positionBeforeUpdate.y;
    }
}
