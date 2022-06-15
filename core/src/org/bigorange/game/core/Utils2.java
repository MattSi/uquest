package org.bigorange.game.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.bigorange.game.UndergroundQuest2;
import org.bigorange.game.WorldContactManager;
import org.bigorange.game.core.dialogue.ConversationManager;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.map.MapManager;

public class Utils2 {
    private Utils2() {
    }

    public static ResourceManager getResourceManager(){
        ApplicationListener app = Gdx.app.getApplicationListener();
        return ((UndergroundQuest2)app).getResourceManager();

    }

    public static UndergroundQuest2 getGameInstance(){
        final ApplicationListener app = Gdx.app.getApplicationListener();
        return (UndergroundQuest2)app;
    }
    public static InputManager getInputManager(){
        return (InputManager)((InputMultiplexer)Gdx.input.getInputProcessor()).getProcessors().get(0);
    }

    public static SpriteBatch getSpriteBatch(){
        ApplicationListener app = Gdx.app.getApplicationListener();
        return ((UndergroundQuest2)app).getSpriteBatch();

    }

    public static MapManager getMapManager(){
        ApplicationListener app = Gdx.app.getApplicationListener();
        return ((UndergroundQuest2)app).getMapManager();
    }

    public static WorldContactManager getWorldContactManager(){
        return ((UndergroundQuest2)Gdx.app.getApplicationListener()).getWorldContactManager();
    }

    public static AudioManager getAudioManager(){
        return ((UndergroundQuest2)Gdx.app.getApplicationListener()).getAudioManager();
    }

    public static ConversationManager getConversationManager(){
        return ((UndergroundQuest2)Gdx.app.getApplicationListener()).getConversationManager();
    }

}
