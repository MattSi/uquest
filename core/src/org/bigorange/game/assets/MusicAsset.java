package org.bigorange.game.assets;


import com.badlogic.gdx.audio.Music;

public enum MusicAsset {
    INTRO("audio/bgm_theme.wav", 0.3f),
    TALKING("audio/castle_bgm_2.wav", 0.3f);

    private final String filePath;
    private final float volume;

    public static final Class klass = Music.class;
    MusicAsset(String filePath, float volume) {
        this.filePath = filePath;
        this.volume = volume;
    }

    public String getFilePath() {
        return filePath;
    }

    public float getVolume() {
        return volume;
    }
}
