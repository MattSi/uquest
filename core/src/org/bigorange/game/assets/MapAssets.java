package org.bigorange.game.assets;

import com.badlogic.gdx.maps.tiled.TiledMap;

public enum MapAssets {
    LEVEL1("map/battle1.tmx"),
    LEVEL2("map/battle2.tmx");

    public static final Class klass = TiledMap.class;

    private final String location;

    MapAssets(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

}
