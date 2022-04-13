package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.WorldContactManager;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.GameObjectComponent;
import org.bigorange.game.ecs.component.PlayerComponent;
import org.bigorange.game.utils.Utils;


public class PlayerContactSystem extends EntitySystem implements WorldContactManager.WorldContactListener {
    public static final String TAG = PlayerContactSystem.class.getSimpleName();
    private final Array<PlayerContactListener> listeners;


    public PlayerContactSystem(){
        Utils.getWorldContactManager().addWorldContactListener(this);
        listeners = new Array<>();
    }

    public void addPlayerContactListener(final PlayerContactListener listener){
        listeners.add(listener);
    }

    @Override
    public void beginContact(Entity player, Entity gameObject) {
        Gdx.app.debug(TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        final GameObjectComponent gameObjCmp = ECSEngine.gameObjCmpMapper.get(gameObject);
        final PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(player);

        switch (gameObjCmp.type){
            case NOT_DEFINED -> {
            }
            case WALL -> {
                wallInteract(playerCmp, gameObjCmp);
            }
        }

    }

    private void wallInteract(PlayerComponent playerCmp, GameObjectComponent gameObjCmp) {
        for (PlayerContactListener listener : listeners) {
            listener.wallContact();
        }

    }

    public interface PlayerContactListener{
        void wallContact();
    }

}
