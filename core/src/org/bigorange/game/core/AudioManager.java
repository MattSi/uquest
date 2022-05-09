package org.bigorange.game.core;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;

public class AudioManager {
    private Music currentMusic;
    private float volume;
    private final ResourceManager resourceManager;

    public AudioManager(){
        this.resourceManager = Utils.getResourceManager();
        this.currentMusic = null;
        this.volume = 1f;
    }

    public void setVolume(final float volume){
        this.volume = MathUtils.clamp(volume, 0f,1f);
        if(currentMusic != null){
            currentMusic.setVolume(volume);
        }
    }
}
