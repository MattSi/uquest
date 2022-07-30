package org.bigorange.game.assets;

import com.badlogic.gdx.audio.Sound;

public enum SoundAsset {
    INTRO("audio/bgm_theme.wav", 1f);

    private final String filePath;
    private final float volume;

    public static final Class klass = Sound.class;
    SoundAsset(String filePath, float volume) {
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
