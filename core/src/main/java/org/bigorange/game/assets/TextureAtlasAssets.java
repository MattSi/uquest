package org.bigorange.game.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public enum TextureAtlasAssets {
    CHARACTERS("characters/characters.atlas"),

    STATUSUI("skins/statusui.atlas"),

    UISKIN("hud/uiskin.atlas");



    public static final Class klass = TextureAtlas.class;
    private final String filePath;
    public String getFilePath() {
        return filePath;
    }
    TextureAtlasAssets(String filePath) {
        this.filePath = filePath;
    }
}
