package org.bigorange.game.assets;

import com.badlogic.gdx.audio.Sound;

public enum SoundAssets {
    INTRO("audio/bgm_theme.wav", 1f);

    private final String location;
    private final float volume;

    public static final Class klass = Sound.class;
    SoundAssets(String location, float volume) {
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
