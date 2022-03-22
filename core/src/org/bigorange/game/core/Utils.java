package org.bigorange.game.core;

import com.badlogic.gdx.Gdx;
import org.bigorange.game.UndergroundQuest;

public class Utils {
    private Utils() {
    }

    public static ResourceManager getResourceManager(){
        return ((UndergroundQuest)Gdx.app.getApplicationListener()).getResourceManager();
    }
}
