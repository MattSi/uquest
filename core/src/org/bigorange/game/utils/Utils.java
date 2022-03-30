package org.bigorange.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.bigorange.game.ResourceManager;
import org.bigorange.game.UndergroundQuest;
import org.bigorange.game.gamestate.EGameState;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.map.MapManager;

public class Utils {
    private Utils() {
    }

    public static ResourceManager getResourceManager(){
        return ((UndergroundQuest)Gdx.app.getApplicationListener()).getResourceManager();
    }

    public static InputManager getInputManager(){
        //return (InputManager)((InputMultiplexer)Gdx.input.getInputProcessor())[0];
        return (InputManager)((InputMultiplexer)Gdx.input.getInputProcessor()).getProcessors().get(0);
    }

    public static SpriteBatch getSpriteBatch(){
        return ((UndergroundQuest)Gdx.app.getApplicationListener()).getSpriteBatch();
    }

    public static MapManager getMapManager(){
        return ((UndergroundQuest)Gdx.app.getApplicationListener()).getMapManager();
    }

    public static void setGameState(final EGameState gameStateType, final boolean disposeActive){
        ((UndergroundQuest)Gdx.app.getApplicationListener()).getGame().setGameState(gameStateType, disposeActive);
    }
}
