package org.bigorange.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.DefaultTimepiece;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import org.bigorange.game.core.AudioManager;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.core.dialogue.ConversationManager;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.map.MapManager;
import org.bigorange.game.screens.GameScreen;
import org.bigorange.game.screens.LoadingScreen;
import org.bigorange.game.screens.MenuScreen;
import org.bigorange.game.ui.TTFSkin;

public class UndergroundQuest2 extends Game {
    private final String TAG = this.getClass().getSimpleName();
    private ResourceManager resourceManager;
    private AudioManager audioManager;
    private MapManager mapManager;
    private WorldContactManager worldContactManager;
    private ConversationManager conversationManager;
    private SpriteBatch spriteBatch;
    private TTFSkin skin;


    private static LoadingScreen loadingScreen;
    private static MenuScreen menuScreen;
    private static GameScreen gameScreen;

    public static enum ScreenType{
        LoadingScreen,
        MenuScreen,
        GameScreen,
    }

    public Screen getScreenType(ScreenType screenType){
        switch (screenType){
            case LoadingScreen -> {
                return loadingScreen;
            }
            case MenuScreen -> {
                return menuScreen;
            }

            case GameScreen -> {
                return gameScreen;
            }
        }
        return null;
    }


    @Override
    public void create() {

        /**
         * 1. Instance managers
         */
        createManagers();

        /**
         * 2. Loading Screen resource
         */
        loadOtherResource();

        /**
         * 3. create screens
         */
        createScreens();

        Gdx.input.setInputProcessor(new InputMultiplexer(new InputManager()));
        setScreen(loadingScreen);
    }

    @Override
    public void render() {
        GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());
        MessageManager.getInstance().update();
        super.render();
    }

    @Override
    public void dispose() {
        disposeScreens();
        disposeManagers();
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    private void createManagers(){
        MessageManager.getInstance().clear();
        MessageManager.getInstance().setDebugEnabled(true);
        GdxAI.setTimepiece(new DefaultTimepiece(0.2f));
        resourceManager = new ResourceManager();
        audioManager = new AudioManager();
        worldContactManager = new WorldContactManager();
        conversationManager = new ConversationManager(Gdx.files.internal("dialogue/conversations.csv"));
        mapManager = new MapManager();

        Gdx.app.debug(TAG, "Create ==================================================");
    }

    private void disposeManagers(){
        resourceManager.dispose();
        audioManager.dispose();
    }

    private void createScreens(){
        loadingScreen = new LoadingScreen(skin);
        menuScreen = new MenuScreen(skin);
        gameScreen = new GameScreen(skin);
    }

    private void disposeScreens(){
        loadingScreen.dispose();
        menuScreen.dispose();
    }

    private void loadOtherResource(){
        spriteBatch = new SpriteBatch();
        resourceManager.load("i18n/strings_zh_CN", I18NBundle .class);
        skin = resourceManager.loadSkinSynchronously("hud/hud.json", "hud/dengl.ttf", 16, 20, 26,32);
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
}
