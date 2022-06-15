package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.WorldContactManager;
import org.bigorange.game.core.Utils;
import org.bigorange.game.core.message.MessageType;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.*;


public class PlayerContactSystem extends EntitySystem implements WorldContactManager.WorldContactListener, TelegramProvider {
    public static final String TAG = PlayerContactSystem.class.getSimpleName();
    private final Array<PlayerContactListener> listeners;


    public PlayerContactSystem(){
        Utils.getWorldContactManager().addWorldContactListener(this);
        listeners = new Array<>();
        MessageManager.getInstance().addProvider(this, MessageType.MSG_PLAYER_LEAVE_NPC);
    }

    public void addPlayerContactListener(final PlayerContactListener listener){
        listeners.add(listener);
    }

    @Override
    public void beginContact(Entity player, Entity gameObject, boolean isSensor) {
        final GameObjectComponent gameObjCmp = ECSEngine.gameObjCmpMapper.get(gameObject);
        final PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(player);
        final EnemyComponent enemyCmp = ECSEngine.enemyCmpMapper.get(gameObject);
        final SteeringComponent steeringCmp = ECSEngine.steerCmpMapper.get(gameObject);
        final SteeringLocationComponent stLocationCmp = ECSEngine.stLocationCmpMapper.get(player);
        final InteractComponent interactCmp = ECSEngine.interactCmpMapper.get(player);


        switch (gameObjCmp.type) {
            case NOT_DEFINED -> {
            }
            case WALL -> {
                wallInteract(playerCmp, gameObjCmp);
            }
            case ENEMY -> {
                if (isSensor) {
                    enemyCmp.findPlayer = true;
                }
                if(steeringCmp.steeringBehavior == null){
                    final Arrive<Vector2> arriveSB = new Arrive<Vector2>(steeringCmp, stLocationCmp)
                            .setTimeToTarget(0.1f)
                            .setArrivalTolerance(0.001f)
                            .setDecelerationRadius(3);
                    //final Seek<Vector2> seekSB = new Seek<>(steeringCmp, stLocationCmp);
                    steeringCmp.steeringBehavior = arriveSB;
                    steeringCmp.targetLocation = stLocationCmp;
                }
            }
            case NPC -> {
                if(isSensor){
                    enemyCmp.findPlayer = true;
                    interactCmp.addInRangeEntity(gameObject);
                }
            }
        }
    }

    @Override
    public void endContact(Entity player, Entity gameObject, boolean isSensor) {
        final GameObjectComponent gameObjCmp = ECSEngine.gameObjCmpMapper.get(gameObject);
        final EnemyComponent enemyCmp = ECSEngine.enemyCmpMapper.get(gameObject);
        final InteractComponent interactCmp = ECSEngine.interactCmpMapper.get(player);


        switch (gameObjCmp.type) {
            case ENEMY, NPC -> {
                if (isSensor) {
                    enemyCmp.findPlayer = false;
                    interactCmp.removeInRangeEntity(gameObject);
                    MessageManager.getInstance().dispatchMessage(MessageType.MSG_PLAYER_LEAVE_NPC);
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
        return this;
    }

    public interface PlayerContactListener{
        void wallContact();
    }

}
