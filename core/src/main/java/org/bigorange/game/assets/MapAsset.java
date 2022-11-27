package org.bigorange.game.assets;

import com.badlogic.gdx.maps.tiled.TiledMap;

public enum MapAsset {
    LEVEL1("map/battle1.tmx"),
    LEVEL2("map/battle2.tmx"),
    ROOM("map/room.tmx"),
    INTRO("map/intro/intro1.tmx");

    public static final Class klass = TiledMap.class;

    private final String filePath;

    MapAsset(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

}
