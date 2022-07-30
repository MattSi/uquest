package org.bigorange.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import org.bigorange.game.UndergroundQuest;
import org.bigorange.game.ui.TTFSkin;

import java.util.EnumMap;


/**
 * 屏幕管理器
 */
public class ScreenManager<S extends BaseScreen> {
    private static final String TAG = ScreenManager.class.getSimpleName();
    private EnumMap<EScreenType, BaseScreen> screenCache;
    private TTFSkin skin;
    private UndergroundQuest gameInstance;

    public ScreenManager(TTFSkin skin, UndergroundQuest gameInstance){
        this.skin = skin;
        this.gameInstance = gameInstance;
        screenCache = new EnumMap<EScreenType, BaseScreen>(EScreenType.class);
    }


    /**
     *
     * @param screenType
     */
    public void setScreenType(final EScreenType screenType){
        BaseScreen targetScreen = screenCache.get(screenType);
        if(targetScreen == null){
            Gdx.app.debug(TAG, "Create new Screen:" + screenType);
            try{
                targetScreen = (BaseScreen) ClassReflection.getConstructor(screenType.getScreenType(),
                        TTFSkin.class, ScreenManager.class).newInstance( skin, this);

                screenCache.put(screenType, targetScreen);
                gameInstance.setScreen(targetScreen);
            }catch (ReflectionException e){
                throw new GdxRuntimeException("Could not create Screen of type: " + screenType, e);
            }
        }
    }

    public < S extends BaseScreen> S getScreenInstance(final EScreenType screenType){
        BaseScreen targetScreen = screenCache.get(screenType);
        if(targetScreen == null){
            Gdx.app.debug(TAG, "Create new Screen:" + screenType);
            try{
                targetScreen = (BaseScreen) ClassReflection.getConstructor(screenType.getScreenType(),
                        TTFSkin.class,ScreenManager.class).newInstance( skin, this);

                screenCache.put(screenType, targetScreen);

            }catch (ReflectionException e){
                throw new GdxRuntimeException("Could not create Screen of type: " + screenType, e);
            }
        }
        return (S)targetScreen;
    }
}
