package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.UserMovementComponent;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.input.KeyInputListener;

import static org.bigorange.game.input.EKey.*;

public class UserMovementSystem extends IteratingSystem implements KeyInputListener {
    private static final String TAG = UserMovementComponent.class.getSimpleName();
    private boolean directionChange;
    private int xFactor;
    private int yFactor;

    public UserMovementSystem() {
        super(Family.all(UserMovementComponent.class).get());
        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final UserMovementComponent usrMoveCmp = ECSEngine.usrMoveCmpMapper.get(entity);

        if(directionChange){
            directionChange = false;
            usrMoveCmp.position.x = xFactor;
            usrMoveCmp.position.y = yFactor;
        }
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {
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

    public void reset(){
        xFactor = yFactor = 0;
        directionChange = false;
    }
}
