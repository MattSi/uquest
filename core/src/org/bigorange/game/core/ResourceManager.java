package org.bigorange.game.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class ResourceManager extends AssetManager {


    public ResourceManager() {
        // Use internal file handler resolver in AssetManager itself
        super();

        final FileHandleResolver resolver = getFileHandleResolver();
        setLoader(TiledMap.class, new TmxMapLoader());
    }
}
