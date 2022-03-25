package org.bigorange.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import org.bigorange.game.core.gamestate.EGameState;
import org.bigorange.game.core.gamestate.GameState;
import org.bigorange.game.core.input.InputManager;
import org.bigorange.game.core.ui.HUD;

import java.util.EnumMap;
import java.util.Map;

public class Game implements Disposable {
    private static final String TAG = Game.class.getSimpleName();
    private static final float TARGET_FRAME_TIME = 1/ 60f;

    private final HUD hud;
    private final EnumMap<EGameState, GameState> gameStateCache;
    private GameState activeState;

    private float accumulator;
    public Game(final EGameState initialStage) {
        gameStateCache = new EnumMap<EGameState, GameState>(EGameState.class);
        accumulator = 0f;

        hud = new HUD();
        Gdx.input.setInputProcessor(new InputMultiplexer(
                new InputManager(),
                hud.getStage()));

        Utils.getInputManager().addKeyInputListener(hud);
        setGameState(initialStage, true);
    }

    private void setGameState(final EGameState gameStateType, boolean disposeActive) {
        if(activeState != null){
            Gdx.app.debug(TAG, "Deactivating gamestate " + (disposeActive ? " and disposing" : "")
                    + " " + activeState.getType());
            activeState.deactivate();
            if(disposeActive){
                gameStateCache.remove(activeState);
                activeState.dispose();
            }
        }

        activeState = gameStateCache.get(gameStateType);
        if(activeState == null){
            Gdx.app.debug(TAG, "Creating new gamestate: " + gameStateType);
            //ClassReflection.getConstructor()
        }
    }

    public void process(){

    }

    public void resize(final int width, final int height){
        activeState.resize(width, height);
    }


    @Override
    public void dispose() {
        for (Map.Entry<EGameState, GameState> entry : gameStateCache.entrySet()) {
            entry.getValue().deactivate();
            entry.getValue().dispose();
        }
        hud.dispose();
    }
}
