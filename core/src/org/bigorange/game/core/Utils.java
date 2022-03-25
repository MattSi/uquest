package org.bigorange.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.bigorange.game.UndergroundQuest;
import org.bigorange.game.core.input.InputManager;

public class Utils {
    private Utils() {
    }

    public static ResourceManager getResourceManager(){
        return ((UndergroundQuest)Gdx.app.getApplicationListener()).getResourceManager();
    }

    public static InputManager getInputManager(){
        return (InputManager)Gdx.input.getInputProcessor();
    }

    public static SpriteBatch getSpriteBatch(){
        return ((UndergroundQuest)Gdx.app.getApplicationListener()).getSpriteBatch();
    }
}
