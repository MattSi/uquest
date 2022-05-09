package org.bigorange.game.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.UndergroundQuest;
import org.bigorange.game.WorldContactManager;
import org.bigorange.game.core.gamestate.EGameState;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.map.MapManager;

public class Utils {
    private Utils() {
    }

    public static ResourceManager getResourceManager(){
        ApplicationListener app = Gdx.app.getApplicationListener();
        return ((UndergroundQuest)app).getResourceManager();

    }

    public static InputManager getInputManager(){
        return (InputManager)((InputMultiplexer)Gdx.input.getInputProcessor()).getProcessors().get(0);
    }

    public static SpriteBatch getSpriteBatch(){
        ApplicationListener app = Gdx.app.getApplicationListener();
        return ((UndergroundQuest)app).getSpriteBatch();

    }

    public static MapManager getMapManager(){
        ApplicationListener app = Gdx.app.getApplicationListener();
        return ((UndergroundQuest)app).getMapManager();
    }

    public static WorldContactManager getWorldContactManager(){
        return ((UndergroundQuest)Gdx.app.getApplicationListener()).getWorldContactManager();
    }

    public static void setGameState(final EGameState gameStateType ){
        setGameState(gameStateType, false);
    }
    public static void setGameState(final EGameState gameStateType, final boolean disposeActive){
        ((UndergroundQuest)Gdx.app.getApplicationListener()).getGame().setGameState(gameStateType, disposeActive);
    }
}
