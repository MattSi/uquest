package org.bigorange.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.DefaultTimepiece;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import org.bigorange.game.core.AudioManager;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.core.dialogue.ConversationManager;
import org.bigorange.game.core.screens.BaseScreen;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.map.MapManager;
import org.bigorange.game.screens.EScreenType;
import org.bigorange.game.ui.TTFSkin;

import java.util.EnumMap;

public class UndergroundQuest extends Game {
    private final String TAG = this.getClass().getSimpleName();
    private EnumMap<EScreenType, BaseScreen> screenCache;
    private ResourceManager resourceManager;
    private AudioManager audioManager;
    private MapManager mapManager;
    private WorldContactManager worldContactManager;
    private ConversationManager conversationManager;
    private SpriteBatch spriteBatch;
    private TTFSkin skin;




    public void setScreenType(final EScreenType screenType){
        BaseScreen targetScreen = screenCache.get(screenType);
        if(targetScreen == null){
            Gdx.app.debug(TAG, "Create new Screen:" + screenType);
            try{
                targetScreen = (BaseScreen) ClassReflection.getConstructor(screenType.getScreenType(),
                         TTFSkin.class).newInstance( skin);

                screenCache.put(screenType, targetScreen);
                setScreen(targetScreen);
            }catch (ReflectionException e){
                throw new GdxRuntimeException("Could not create Screen of type: " + screenType, e);
            }
        }
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
        screenCache = new EnumMap<EScreenType, BaseScreen>(EScreenType.class);

        Gdx.input.setInputProcessor(new InputMultiplexer(new InputManager()));
       // setScreen(loadingScreen);
        setScreenType(EScreenType.LOADING);
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

    private void disposeScreens(){
        for (BaseScreen value : screenCache.values()) {
            value.dispose();
        }
    }

    private void loadOtherResource(){
        spriteBatch = new SpriteBatch();
        resourceManager.load("i18n/strings_zh_CN", I18NBundle .class);
        skin = resourceManager.loadSkinSynchronously("hud/hud.json", "hud/simsun.ttc", 16, 20, 26,32);
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
