package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.WorldContactManager;
import org.bigorange.game.Utils;
import org.bigorange.game.message.MessageType;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.*;


public class PlayerContactSystem extends EntitySystem implements
        WorldContactManager.WorldContactListener,
        TelegramProvider {
    public static final String TAG = PlayerContactSystem.class.getSimpleName();
    private final Array<PlayerContactListener> listeners;
    private ActionableComponent actionCmp;


    public PlayerContactSystem(){
        Utils.getWorldContactManager().addWorldContactListener(this);
        listeners = new Array<>();
        MessageManager.getInstance().addProvider(this, MessageType.PLAYER_AWAY_FROM_NPC);
        MessageManager.getInstance().addProvider(this, MessageType.PLAYER_CLOSE_TO_NPC);
        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }


    public void addPlayerContactListener(final PlayerContactListener listener){
        listeners.add(listener);
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
                    actionCmp = actionCmpTmp;
                    MessageManager.getInstance().dispatchMessage(MessageType.PLAYER_CLOSE_TO_NPC, actionCmp );
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
                    MessageManager.getInstance().dispatchMessage(MessageType.PLAYER_AWAY_FROM_NPC);
                    Gdx.app.debug(TAG, "Player leaved NPC.");
                }
            }
        }
    }

    private void wallInteract(PlayerComponent playerCmp, GameObjectComponent gameObjCmp) {
        for (PlayerContactListener listener : listeners) {
            listener.wallContact();
        }

    }

    @Override
    public Object provideMessageInfo(int msg, Telegraph receiver) {
        return actionCmp;
    }

    public interface PlayerContactListener{
        void wallContact();
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        listeners.clear();
        MessageManager.getInstance().clearProviders(MessageType.PLAYER_AWAY_FROM_NPC);
    }
}
