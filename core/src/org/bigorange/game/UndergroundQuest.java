package org.bigorange.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.DefaultTimepiece;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import org.bigorange.game.audio.AudioManager;
import org.bigorange.game.dialogue.ConversationManager;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.map.MapManager;
import org.bigorange.game.screens.EScreenType;
import org.bigorange.game.screens.ScreenManager;
import org.bigorange.game.ui.TTFSkin;

public class UndergroundQuest extends Game {
    private final String TAG = this.getClass().getSimpleName();

    private ResourceManager resourceManager;
    private AudioManager audioManager;
    private MapManager mapManager;
    private WorldContactManager worldContactManager;
    private ConversationManager conversationManager;
    private ScreenManager screenManager;
    private InputManager inputManager;
    private SpriteBatch spriteBatch;
    private TTFSkin skin;


    @Override
    public void create() {


        /**
         * 1. Instance managers
         */
        MessageManager.getInstance().clear();
        MessageManager.getInstance().setDebugEnabled(true);
        GdxAI.setTimepiece(new DefaultTimepiece(0.2f));
        resourceManager = new ResourceManager();
        audioManager = new AudioManager();
        worldContactManager = new WorldContactManager();
        conversationManager = new ConversationManager(Gdx.files.internal("dialogue/conversations.csv"));
        mapManager = new MapManager();
        inputManager = new InputManager();


        Gdx.app.debug(TAG, "Create ==================================================");

        /**
         * 2. Loading Screen resource
         */
        spriteBatch = new SpriteBatch();
        resourceManager.load("i18n/strings_zh_CN", I18NBundle .class);
        skin = resourceManager.loadSkinSynchronously("hud/uiskin.json", "hud/simsun.ttc", 16, 20, 26,32);

        /**
         * 3. create screens
         */
        screenManager = new ScreenManager(skin, this);

        Gdx.input.setInputProcessor(new InputMultiplexer(inputManager));
       // setScreen(loadingScreen);
        screenManager.setScreenType(EScreenType.LOADING);
    }

    @Override
    public void render() {
        GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());
        MessageManager.getInstance().update();
        super.render();
    }

    @Override
    public void dispose() {
        //disposeScreens();
        disposeManagers();
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    private void createManagers(){

    }

    private void disposeManagers(){
        resourceManager.dispose();
        audioManager.dispose();
    }

    private void loadOtherResource(){

    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public WorldContactManager getWorldContactManager() {
        return worldContactManager;
    }

    public ConversationManager getConversationManager() {
        return conversationManager;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }
}
