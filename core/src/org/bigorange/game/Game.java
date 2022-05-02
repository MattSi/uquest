package org.bigorange.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import org.bigorange.game.gamestate.EGameState;
import org.bigorange.game.gamestate.GameState;
import org.bigorange.game.gamestate.State;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.ui.HUD;

import java.util.EnumMap;
import java.util.Map;

public class Game implements Disposable {
    private static final String TAG = Game.class.getSimpleName();
    public static final float TARGET_FRAME_TIME = 1 / 60f;

    private final HUD hud;
    private final EnumMap<EGameState, GameState> gameStateCache;
    private GameState activeState;

    private float accumulator;

    private State state = State.RUNNING;

    public Game(final EGameState initialStage) {
        gameStateCache = new EnumMap<EGameState, GameState>(EGameState.class);
        accumulator = 0f;

        MessageManager.getInstance().clear();
        hud = new HUD();
        Gdx.input.setInputProcessor(new InputMultiplexer(new InputManager()));

        setGameState(initialStage, true);
    }

    public void setGameState(final EGameState gameStateType, boolean disposeActive) {
        /**
         * 1. 已有的gameState做deactivate处理
         * 2. 根据给定的参数，判断gameState需不需要dispose
         *
         * 3. 用反射的方法生成gameState的示例
         *   (ClassReflection.getConstructor)
         * 4. EnumMap类的使用
         */
        if (activeState != null) {
            Gdx.app.debug(TAG, "Deactivating gamestate " + (disposeActive ? " and disposing" : "")
                    + " " + activeState.getType());
            activeState.deactivate();
            if (disposeActive) {
                gameStateCache.remove(activeState);
                activeState.dispose();
            }
        }

        activeState = gameStateCache.get(gameStateType);
        if (activeState == null) {
            Gdx.app.debug(TAG, "Creating new gamestate: " + gameStateType);
            try {
                activeState = (GameState) ClassReflection.getConstructor(gameStateType.getGameStateType(),
                        EGameState.class, HUD.class).newInstance(gameStateType, hud);
                gameStateCache.put(gameStateType, activeState);
            } catch (ReflectionException e) {
                throw new GdxRuntimeException("Could not create gamestate of type: " + gameStateType, e);
            }
        }

        Gdx.app.debug(TAG, "Activating gamestate " + activeState.getType());
        activeState.activate();
        activeState.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void process() {
        final float deltaTime = Gdx.graphics.getDeltaTime();
        accumulator += deltaTime > 0.25f ? 0.25f : deltaTime;

        while (accumulator >= TARGET_FRAME_TIME) {
            activeState.step(TARGET_FRAME_TIME);
            accumulator -= TARGET_FRAME_TIME;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        activeState.render(accumulator / TARGET_FRAME_TIME);

        MessageManager.getInstance().update();
    }

    public void resize(final int width, final int height) {
        activeState.resize(width, height);
    }


    @Override
    public void dispose() {
        for (Map.Entry<EGameState, GameState> entry : gameStateCache.entrySet()) {
            Gdx.app.debug(TAG, "Disposing gamestate: " + entry.getKey());
            entry.getValue().deactivate();
            entry.getValue().dispose();
        }
    }
}
