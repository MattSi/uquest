package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.Box2DComponent;
import org.bigorange.game.ecs.component.PlayerComponent;
import org.bigorange.game.input.*;

import static org.bigorange.game.input.EKey.*;

public class PlayerMovementSystem extends IteratingSystem implements KeyInputListener, MouseInputListener {
    public static final String TAG = PlayerMovementSystem.class.getSimpleName();
    private boolean directionChange;
    private int xFactor;
    private int yFactor;

    private final OrthographicCamera camera;
    private final ECSEngine ecsEngine;

    private boolean isShooting;
    private Vector3 target;


    public PlayerMovementSystem(ECSEngine ecsEngine, OrthographicCamera camera) {
        super(Family.all(PlayerComponent.class).get());
        this.ecsEngine = ecsEngine;
        this.camera = camera;
        directionChange = false;
        xFactor = 0;
        yFactor = 0;
        Gdx.app.debug(TAG,  " instantiated.");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);
        b2dCmp.positionBeforeUpdate.set(b2dCmp.body.getPosition());

        if (directionChange) {
            directionChange = false;
            playerCmp.speed.x = xFactor * playerCmp.maxSpeed;
            playerCmp.speed.y = yFactor * playerCmp.maxSpeed;
        }

        final Vector2 worldCenter = b2dCmp.body.getWorldCenter();
        b2dCmp.body.applyLinearImpulse(
                (playerCmp.speed.x - b2dCmp.body.getLinearVelocity().x) * b2dCmp.body.getMass(),
                (playerCmp.speed.y - b2dCmp.body.getLinearVelocity().y) * b2dCmp.body.getMass(),
                worldCenter.x, worldCenter.y, true);

        if(isShooting){
            isShooting = false;
            ecsEngine.addBullet(b2dCmp.body.getPosition(), new Vector2(target.x, target.y));
            Gdx.app.debug(TAG, "Body: "+b2dCmp.body.getPosition() + " Target: " + target.toString());
        }
    }

    @Override
    public void buttonDown(InputManager manager, EMouse mouse, Vector2 vector2) {
        switch (mouse){
            case LEFT -> {
                isShooting = true;
                target = camera.unproject(new Vector3(vector2.x, vector2.y, 0));
            }
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



    @Override
    public void buttonUp(InputManager manager, EMouse mouse, Vector2 vector2) {

    }
}
