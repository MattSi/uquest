package org.bigorange.game.core.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public enum TextureAtlasAssets {
    CHARACTERS("characters/characters.atlas");



    public static final Class klass = TextureAtlas.class;
    private final String filePath;
    public String getFilePath() {
        return filePath;
    }
    TextureAtlasAssets(String filePath) {
        this.filePath = filePath;
    }
}
