package org.bigorange.game.core;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import org.bigorange.game.core.assets.MusicAsset;
import org.bigorange.game.core.assets.SoundAsset;

public class AudioManager implements Disposable {
    private Music currentMusic;
    private MusicAsset currentMusicAsset;

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

    public float getVolume() {
        return volume;
    }
    public void playSound(final SoundAsset soundAsset){
        final Sound sound = resourceManager.get(soundAsset.getFilePath(), Sound.class);
        sound.play(soundAsset.getVolume() * volume);
    }

    public void playMusic(final MusicAsset musicAsset){
        final Music music = resourceManager.get(musicAsset.getFilePath(), Music.class);
        if(currentMusicAsset == musicAsset){
            return;
        } else if(currentMusic != null){
            currentMusic.stop();
        }

        currentMusicAsset = musicAsset;
        currentMusic = music;
        currentMusic.setLooping(true);
        currentMusic.setVolume(currentMusicAsset.getVolume() * volume);
        currentMusic.play();
    }

    @Override
    public void dispose() {
        if(currentMusic != null){
            currentMusic.dispose();
            currentMusic = null;
        }
    }
}
