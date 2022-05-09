package org.bigorange.game.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public enum TextureAtlasAssets {
    CHARACTERS("characters/characters.atlas");



    public static final Class klass = TextureAtlas.class;
    private final String location;
    public String getLocation() {
        return location;
    }
    TextureAtlasAssets(String location) {
        this.location = location;
    }
}
