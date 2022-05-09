package org.bigorange.game.assets;


import com.badlogic.gdx.audio.Music;

public enum MusicAssets {
    INTRO("audio/bgm_theme.wav", 0.3f),
    TALKING("audio/castle_bgm_2.wav", 0.3f);

    private final String location;
    private final float volume;

    public static final Class klass = Music.class;
    MusicAssets(String location, float volume) {
        this.location = location;
        this.volume = volume;
    }

    public String getMusicLocation() {
        return location;
    }

    public float getVolume() {
        return volume;
    }
}
