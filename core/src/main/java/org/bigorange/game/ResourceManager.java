package org.bigorange.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.ui.SkinLoader;
import org.bigorange.game.ui.TTFSkin;

import java.util.Locale;

public class ResourceManager extends AssetManager {
    private Locale currentLocale;
    private Array<LocaleListener> listeners;

    public ResourceManager() {
        // Use internal file handler resolver in AssetManager itself
        super();

        final FileHandleResolver resolver = getFileHandleResolver();
        setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        setLoader(TTFSkin.class, new SkinLoader(resolver));
        setLoader(TiledMap.class, new TmxMapLoader());
        listeners = new Array<>();
    }

    public TTFSkin loadSkinSynchronously(final String skinJsonFilePath, final String ttfFilePath, final int... fontSizeToCreate){
        load(skinJsonFilePath, TTFSkin.class, new SkinLoader.SkinParameter(ttfFilePath, fontSizeToCreate));
        finishLoading();

        return get(skinJsonFilePath, TTFSkin.class);
    }


    public void addLocaleListener(LocaleListener listener){
        if(!listeners.contains(listener, true)){
            listeners.add(listener);
        }
    }

    public void removeLocaleListener(LocaleListener listener){
        listeners.removeValue(listener, true);
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public interface LocaleListener{
        void localeChanged(Locale locale);
    }
}
