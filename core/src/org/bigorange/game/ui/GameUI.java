package org.bigorange.game.ui;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.I18NBundle;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.core.Utils;
import org.bigorange.game.core.dialogue.DialogueNode;
import org.bigorange.game.core.ui.BaseUI;
import org.bigorange.game.message.MessageType;

public class GameUI extends BaseUI {
    private final TextButton infoBox;
    private final TextButton infoBox2;
    private final ProgressBar progressBar;
    private I18NBundle i18NBundle;



    public GameUI( final TTFSkin skin){
        super(skin);

        progressBar = new ProgressBar(0, 100, 1f, false, skin, "default");
        progressBar.setValue(50f);
        infoBox = new TextButton("", skin,"info_frame");
        infoBox2 = new TextButton("", skin, "info_frame");

        //infoBox.getLabel().setWrap(true);
        infoBox.setVisible(false);
        infoBox2.setVisible(false);


        add(progressBar).left().pad(5,5,5,5).row();
       // add(infoBox).expand().pad(5, 5, 5, 5).bottom();
        Table infoBoxTable = new Table();
        infoBoxTable.add(infoBox).expandX().row();
        infoBoxTable.add(infoBox2).expandX();
        //add(infoBox2).expand().bottom();
        //add(infoBox).expand().bottom();
        add(infoBoxTable).expand().bottom();

        // Add Message listener
        MessageManager.getInstance().addListener(this, MessageType.MSG_PLAYER_TALK_TO_NPC);
        MessageManager.getInstance().addListener(this, MessageType.MSG_PLAYER_LEAVE_NPC);

        final ResourceManager resourceManager = Utils.getResourceManager();
        i18NBundle = resourceManager.get("i18n/strings_zh_CN", I18NBundle.class);
        //debugAll();
    }

    public void showInfoMessage(final String message, boolean isVisible) {
        infoBox.setVisible(isVisible);
        infoBox2.setVisible(false);

        infoBox.setText(message);
    }
    public void showChoiceMessage(final String message1, final String message2, boolean isVisible){
        infoBox.setVisible(isVisible);
        infoBox2.setVisible(isVisible);

        infoBox.setText(message1);
        infoBox2.setText(message2);
    }


    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message){
            case MessageType.MSG_PLAYER_TALK_TO_NPC->{
                DialogueNode dialogueNode = (DialogueNode) msg.extraInfo;

                switch (dialogueNode.getNodeType()){
                    case END -> {
                        showInfoMessage("", false);
                    }
                    case MESSAGE -> {
                        showInfoMessage( i18NBundle.format("T"+ dialogueNode.getMessageId()), true);
                    }
                    case CHOICE -> {
                        //dialogueNode.getChoice().get(0)
                        showChoiceMessage(i18NBundle.format("T"+dialogueNode.getChoice().get(0).getMessage()),
                                i18NBundle.format("T"+dialogueNode.getChoice().get(1).getMessage()),
                                true);
                    }
                }

            }
            case MessageType.MSG_PLAYER_LEAVE_NPC -> {
                showInfoMessage("",false);
            }
        }

        return true;
    }
}
