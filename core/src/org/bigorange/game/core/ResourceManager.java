package org.bigorange.game.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.I18NBundle;
import org.bigorange.game.ui.SkinLoader;
import org.bigorange.game.ui.TTFSkin;

public class ResourceManager extends AssetManager {


    public ResourceManager() {
        // Use internal file handler resolver in AssetManager itself
        super();

        final FileHandleResolver resolver = getFileHandleResolver();
        setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        setLoader(TTFSkin.class, new SkinLoader(resolver));
        setLoader(TiledMap.class, new TmxMapLoader());
    }

    public TTFSkin loadSkinSynchronously(final String skinJsonFilePath, final String ttfFilePath, final int... fontSizeToCreate){
        load(skinJsonFilePath, TTFSkin.class, new SkinLoader.SkinParameter(ttfFilePath, fontSizeToCreate));
        finishLoading();

        return get(skinJsonFilePath, TTFSkin.class);
    }

}
