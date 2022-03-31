package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.Box2DComponent;
import org.bigorange.game.ecs.component.PlayerComponent;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.input.KeyInputListener;

import static org.bigorange.game.input.EKey.*;

public class PlayerMovenentSystem extends IteratingSystem implements KeyInputListener {
    public static final String TAG = PlayerMovenentSystem.class.getSimpleName();
    private boolean directionChange;
    private int xFactor;
    private int yFactor;

    public PlayerMovenentSystem() {
        super(Family.all(PlayerComponent.class).get());
        directionChange = false;
        xFactor = 0;
        yFactor = 0;
        Gdx.app.debug(TAG,  " instantiated.");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);

        if(directionChange){
            directionChange = false;
            playerCmp.speed.x = xFactor * playerCmp.maxSpeed;
            playerCmp.speed.y = yFactor * playerCmp.maxSpeed;


        }
        b2dCmp.x += playerCmp.speed.x * deltaTime;
        b2dCmp.y += playerCmp.speed.y * deltaTime;
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {
        Gdx.app.debug(TAG, "Key Down.");
        switch (key) {
            case LEFT:
                directionChange = true;
                xFactor = -1;
                break;
            case RIGHT:
                directionChange = true;
                xFactor = 1;
                break;
            case UP:
                directionChange = true;
                yFactor = 1;
                break;
            case DOWN:
                directionChange = true;
                yFactor = -1;
                break;
            default:
                // nothing to do
                break;
        }
    }

    @Override
    public void keyUP(InputManager manager, EKey key) {
        switch (key) {
            case LEFT:
                directionChange = true;
                xFactor = manager.isKeyDown(RIGHT) ? 1 : 0;
                break;
            case RIGHT:
                directionChange = true;
                xFactor = manager.isKeyDown(LEFT) ? -1 : 0;
                break;
            case UP:
                directionChange = true;
                yFactor = manager.isKeyDown(DOWN) ? -1 : 0;
                break;
            case DOWN:
                directionChange = true;
                yFactor = manager.isKeyDown(UP) ? 1 : 0;
                break;
            default:
                // nothing to do
                break;
        }
    }
}
