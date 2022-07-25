package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.bigorange.game.Utils;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.component.*;
import org.bigorange.game.input.*;
import org.bigorange.game.message.MessageType;

import static org.bigorange.game.input.EKey.*;

public class PlayerControlSystem extends IteratingSystem implements
        InputListener,
        TelegramProvider,
        Telegraph {
    public static final String TAG = PlayerControlSystem.class.getSimpleName();
    private boolean directionChange;
    private int xFactor;
    private int yFactor;

    private final OrthographicCamera camera;
    private final ECSEngine ecsEngine;

    private boolean isShooting;
    private Vector3 shootingTarget;
    private boolean actionPressed = false;
    private boolean selectPressed = false;
    protected boolean keyInputListenerEnabled;

    public PlayerControlSystem(ECSEngine ecsEngine, OrthographicCamera camera) {
        super(Family.all(PlayerComponent.class, Box2DComponent.class,
                AnimationComponent2.class).get());
        this.ecsEngine = ecsEngine;
        this.camera = camera;
        directionChange = false;
        xFactor = 0;
        yFactor = 0;
        shootingTarget = new Vector3(0, 0, 0);
        keyInputListenerEnabled = true;
        MessageManager.getInstance().addProvider(this, MessageType.MSG_PLAYER_CHANGE_MAP);

        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerComponent playerCmp = EntityEngine.playerCmpMapper.get(entity);
        final Box2DComponent b2dCmp = EntityEngine.b2dCmpMapper.get(entity);
        final SpeedComponent speedCmp = EntityEngine.speedCmpMapper.get(entity);
        final InteractComponent interactCmp = EntityEngine.interactCmpMapper.get(entity);
        b2dCmp.positionBeforeUpdate.set(b2dCmp.body.getPosition());


        if (directionChange) {
            directionChange = false;
            speedCmp.velocity.x = xFactor * speedCmp.maxSpeed;
            speedCmp.velocity.y = yFactor * speedCmp.maxSpeed;
        }

        final Vector2 worldCenter = b2dCmp.body.getWorldCenter();
        b2dCmp.body.applyLinearImpulse(
                (speedCmp.velocity.x - b2dCmp.body.getLinearVelocity().x) * b2dCmp.body.getMass(),
                (speedCmp.velocity.y - b2dCmp.body.getLinearVelocity().y) * b2dCmp.body.getMass(),
                worldCenter.x, worldCenter.y, true);

        if (isShooting) {
            isShooting = false;
            ecsEngine.addBullet(b2dCmp.body.getPosition(), new Vector2(shootingTarget.x, shootingTarget.y));
        }

        // Process Action
        if (actionPressed) {
            actionPressed = false;

            interactCmp.interact = true;
        }

        if (selectPressed) {
            selectPressed = false;
            if(playerCmp.rollingTriggerOff()) {
                entity.add(ecsEngine.createComponent(RollingComponent.class));
            }
        }
//        Gdx.app.log(TAG, "VX: " + speedCmp.velocity.x + " VY:" + speedCmp.velocity.y +
//                " XF: " + xFactor + "  YF: " + yFactor + "DirChg:" + directionChange);
    }


    @Override
    public void mouseMoved(InputManager manager, Vector2 screenPoint) {

    }

    @Override
    public void buttonDown(InputManager manager, EMouse mouse, Vector2 screenPoint, int pointer) {
        switch (mouse) {
            case LEFT -> {
                isShooting = true;
                shootingTarget.set(screenPoint.x, screenPoint.y, 0);
                camera.unproject(shootingTarget);
                //shootingTarget = camera.unproject(new Vector3(screenPoint.x, screenPoint.y, 0));
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
            case Action:
                actionPressed = true;
                break;
            case Map:
                MessageManager.getInstance().dispatchMessage(0.0f, this, MessageType.MSG_PLAYER_CHANGE_MAP, "map/battle2.tmx");
                break;
            case SELECT:
                selectPressed = true;
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
            case SELECT:
                selectPressed = true;
                break;
            default:
                // nothing to do
                break;
        }
    }

    public void refreshKeys() {
        final InputManager manager = Utils.getInputManager();
        int left = manager.isKeyDown(LEFT) ? -1 : 0;
        int right = manager.isKeyDown(RIGHT) ? 1 : 0;
        int up = manager.isKeyDown(UP) ? 1 : 0;
        int down = manager.isKeyDown(DOWN) ? -1 : 0;

        xFactor = right + left;
        yFactor = up + down;

        // todo, fixme, using BOX2D DYNAMIC
        directionChange = true;

    }

    @Override
    public void setEnabled(boolean enabled) {
        keyInputListenerEnabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return keyInputListenerEnabled;
    }


    @Override
    public void buttonUp(InputManager manager, EMouse mouse, Vector2 screenPoint, int pointer) {

    }

    @Override
    public Object provideMessageInfo(int msg, Telegraph receiver) {
        return this;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }

}
