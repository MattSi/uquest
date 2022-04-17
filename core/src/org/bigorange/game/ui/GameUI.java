package org.bigorange.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GameUI extends Table {
    private final TextButton infoBox;
    private final ProgressBar progressBar;

    private final TTFSkin skin;

    public GameUI( final TTFSkin skin){
        super();
        this.skin = skin;

        progressBar = new ProgressBar(0, 100, 1f, false, skin, "default");
        progressBar.setValue(50f);
        infoBox = new TextButton("XXXXXXXXXX", skin,"info_frame");
        infoBox.getLabel().setWrap(true);
        infoBox.setVisible(false);


        add(progressBar).left().pad(5,5,5,5).row();
        add(infoBox).expand().pad(5, 5, 5, 5).bottom();
        //debugAll();
    }

    public void showInfoMessage(final String message, final float displayTime) {
        infoBox.setText(message);
        infoBox.setVisible(true);
        //infoBox.setColor(1, 1, 1, 0);
        //infoBox.clearActions();

        infoBox.addAction(sequence( delay(displayTime), hide()));
    }
}
