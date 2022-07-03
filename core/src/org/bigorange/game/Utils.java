package org.bigorange.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.bigorange.game.dialogue.ConversationManager;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.map.MapManager;
import org.bigorange.game.screens.ScreenManager;

public class Utils {
    private Utils() {
    }

    public static ResourceManager getResourceManager(){
        ApplicationListener app = Gdx.app.getApplicationListener();
        return ((UndergroundQuest)app).getResourceManager();

    }

    public static UndergroundQuest getGameInstance(){
        final ApplicationListener app = Gdx.app.getApplicationListener();
        return (UndergroundQuest)app;
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

    public static AudioManager getAudioManager(){
        return ((UndergroundQuest)Gdx.app.getApplicationListener()).getAudioManager();
    }

    public static ConversationManager getConversationManager(){
        return ((UndergroundQuest)Gdx.app.getApplicationListener()).getConversationManager();
    }

    public static ScreenManager getScreenManager(){
        return ((UndergroundQuest)Gdx.app.getApplicationListener()).getScreenManager();
    }
}
