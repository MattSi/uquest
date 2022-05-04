package org.bigorange.game.ui;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import org.bigorange.game.core.ui.BaseUI;
import org.bigorange.game.message.MessageType;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GameUI extends BaseUI {
    private final TextButton infoBox;
    private final ProgressBar progressBar;



    public GameUI( final TTFSkin skin){
        super(skin);

        progressBar = new ProgressBar(0, 100, 1f, false, skin, "default");
        progressBar.setValue(50f);
        infoBox = new TextButton("XXXXXXXXXX", skin,"info_frame");
        infoBox.getLabel().setWrap(true);
        infoBox.setVisible(false);


        add(progressBar).left().pad(5,5,5,5).row();
        add(infoBox).expand().pad(5, 5, 5, 5).bottom();

        // Add Message listener
        MessageManager.getInstance().addListener(this, MessageType.MSG_PLAYER_TALK_TO_NPC);

        //debugAll();
    }

    public void showInfoMessage(final String message, final float displayTime) {
        infoBox.setText(message);
        infoBox.setVisible(true);

        infoBox.addAction(sequence( delay(displayTime), hide()));
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        String message = (String) msg.extraInfo;

        showInfoMessage(message, 3f);
        return true;
    }
}
