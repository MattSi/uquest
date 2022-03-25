package org.bigorange.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LoadingUI extends Table {
    private final TextButton pressAnyButtonInfo;
    private final ProgressBar progressBar;

    public LoadingUI(){
        pressAnyButtonInfo = null;
        progressBar = null;
    }
}
