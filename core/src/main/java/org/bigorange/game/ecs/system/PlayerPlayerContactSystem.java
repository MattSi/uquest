package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import com.badlogic.gdx.ai.msg.Telegraph;
import org.bigorange.game.Utils;
import org.bigorange.game.WorldContactManager;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.*;
import org.bigorange.game.message.MessageType;

/**
 * Player同NPC与Enemy接触的System， 通过Box2D触发
 */
public class PlayerPlayerContactSystem extends EntitySystem implements
        WorldContactManager.WorldPlayerContactListener,
        TelegramProvider {
    public static final String TAG = PlayerPlayerContactSystem.class.getSimpleName();

    public PlayerPlayerContactSystem(){
        Utils.getWorldContactManager().addWorldPlayerContactListener(this);
        MessageManager.getInstance().addProvider(this, MessageType.PLAYER_AWAY_FROM_NPC);
        MessageManager.getInstance().addProvider(this, MessageType.PLAYER_CLOSE_TO_NPC);
        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }

    @Override
    public void beginContact(Entity player, Entity cpuGameObj, boolean isPlayerSensor, boolean isGameObjSensor) {
        final PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(player);
        final CpuComponent cpuCmp = ECSEngine.cpuCmpMapper.get(cpuGameObj);
        final ActionableComponent actionCmpTmp = ECSEngine.actionCmpMapper.get(cpuGameObj);

        final SteeringComponent steeringCmp = ECSEngine.steerCmpMapper.get(cpuGameObj);
        final SteeringLocationComponent stLocationCmp = ECSEngine.stLocationCmpMapper.get(player);
        final InteractComponent interactCmp = ECSEngine.interactCmpMapper.get(player);

        switch (cpuCmp.gameObjectType){
            case NOT_DEFINED -> {
            }
            case NPC -> {
                if(isGameObjSensor){
                    cpuGameObj.add(getEngine().createComponent(CpuCmpClosedToPlayerComponent.class));
                    MessageManager.getInstance().dispatchMessage(MessageType.PLAYER_CLOSE_TO_NPC, actionCmpTmp);
                    interactCmp.addInRangeEntity(cpuGameObj);
                    Gdx.app.debug(TAG, "Player close to NPC, Press E key to talk to this NPC.");
                }
            }
        }

//        switch (gameObjCmp.type) {
//            case NOT_DEFINED -> {
//            }
//            case WALL -> {
//                wallInteract(playerCmp, gameObjCmp);
//            }
//            case ENEMY -> {
//                if (isGameObjSensor) {
//                    gameObjCmp.findPlayer = true;
//                }
//                if(steeringCmp.steeringBehavior == null){
//                    final Arrive<Vector2> arriveSB = new Arrive<Vector2>(steeringCmp, stLocationCmp)
//                            .setTimeToTarget(0.1f)
//                            .setArrivalTolerance(0.001f)
//                            .setDecelerationRadius(3);
//                    //final Seek<Vector2> seekSB = new Seek<>(steeringCmp, stLocationCmp);
//                    steeringCmp.steeringBehavior = arriveSB;
//                    steeringCmp.targetLocation = stLocationCmp;
//                }
//            }
//            case NPC -> {
//
//                if(isGameObjSensor){
//                    gameObjCmp.findPlayer = true;
//                    interactCmp.addInRangeEntity(gameObject);
//
//                }
//            }
//        }
    }

    @Override
    public void endContact(Entity player, Entity cpuGameObj, boolean isPlayerSensor, boolean isGameObjSensor) {
        final InteractComponent interactCmp = ECSEngine.interactCmpMapper.get(player);
        final CpuComponent cpuCmp = ECSEngine.cpuCmpMapper.get(cpuGameObj);


        switch (cpuCmp.gameObjectType) {
            case ENEMY, NPC -> {
                if (isGameObjSensor) {
                    interactCmp.removeInRangeEntity(cpuGameObj);
                    cpuGameObj.remove(CpuCmpClosedToPlayerComponent.class);
                    MessageManager.getInstance().dispatchMessage(MessageType.PLAYER_AWAY_FROM_NPC);
                    Gdx.app.debug(TAG, "Player leaved NPC.");
                }
            }
        }
    }

    @Override
    public Object provideMessageInfo(int msg, Telegraph receiver) {
        return null;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        MessageManager.getInstance().clearProviders(MessageType.PLAYER_AWAY_FROM_NPC);
    }
}
