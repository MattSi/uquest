package org.bigorange.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.core.Utils;
import org.bigorange.game.core.dialogue.Choice;
import org.bigorange.game.core.dialogue.DialogueNode;
import org.bigorange.game.core.ui.BaseUI;
import org.bigorange.game.message.MessageType;

import java.util.List;

public class GameUI extends BaseUI implements TelegramProvider {
    private static final String TAG = GameUI.class.getSimpleName();
    private final DialogueBox infoBox;
    private final DialogueBox infoBox2;
    private final ProgressBar progressBar;
    private I18NBundle i18NBundle;



    public GameUI(final HUD hud, final TTFSkin skin){
        super(skin);

        MessageManager.getInstance().addProvider(this, MessageType.MSG_PLAYER_TALK_TO_NPC);
        progressBar = new ProgressBar(0, 100, 1f, false, skin, "default");
        progressBar.setValue(50f);
        infoBox = new DialogueBox("", skin,"info_frame");
        infoBox2 = new DialogueBox("", skin, "info_frame");

        infoBox.setVisible(false);
        infoBox2.setVisible(false);

        infoBox.addListener((Event e) ->{
            if (!(e instanceof InputEvent) ||
                    !((InputEvent)e).getType().equals(InputEvent.Type.touchDown))
                return false;
            if(infoBox.getDialogueNode() != null) {
                MessageManager.getInstance().dispatchMessage(0.2f, MessageType.MSG_PLAYER_TALK_TO_NPC, infoBox.getDialogueNode());
            }
            return false;
        });

        infoBox2.addListener((Event e) ->{
            if (!(e instanceof InputEvent) ||
                    !((InputEvent)e).getType().equals(InputEvent.Type.touchDown))
                return false;
            if(infoBox2.getDialogueNode() != null) {
                MessageManager.getInstance().dispatchMessage(0.2f, MessageType.MSG_PLAYER_TALK_TO_NPC, infoBox2.getDialogueNode());
            }
            return false;
        });



        add(progressBar).left().pad(5,5,5,5).row();
        Table infoBoxTable = new Table();
        infoBoxTable.add(infoBox2).expandX().row();
        infoBoxTable.add(infoBox).expandX();
        add(infoBoxTable).expand().bottom();

        // Add Message listener
        MessageManager.getInstance().addListener(this, MessageType.MSG_NPC_TALK_TO_PLAYER);
        MessageManager.getInstance().addListener(this, MessageType.MSG_PLAYER_LEAVE_NPC);

        final ResourceManager resourceManager = Utils.getResourceManager();
        i18NBundle = resourceManager.get("i18n/strings_zh_CN", I18NBundle.class);
        //debugAll();
    }

    public void showInfoMessage(final String message, boolean isVisible) {
        infoBox.setVisible(isVisible);
        infoBox2.setVisible(false);
        infoBox2.setDialogueNode(null);
        infoBox.setDialogueNode(null);

        infoBox.setText(message);
    }

    public void showChoiceMessage(final Choice choice1, final Choice choice2, boolean isVisible) {
        infoBox.setVisible(isVisible);
        infoBox2.setVisible(isVisible);

        infoBox.setDialogueNode(choice1.getNextNode());
        infoBox.setText(i18NBundle.format("T" + choice1.getMessage()));

        infoBox2.setDialogueNode(choice2.getNextNode());
        infoBox2.setText(i18NBundle.format("T" + choice2.getMessage()));

    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message){
            case MessageType.MSG_NPC_TALK_TO_PLAYER ->{
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
                        final List<Choice> choiceDialogue = dialogueNode.getChoice();
                        showChoiceMessage(choiceDialogue.get(0), choiceDialogue.get(1), true);
                    }
                }

            }
            case MessageType.MSG_PLAYER_LEAVE_NPC -> {
                showInfoMessage("",false);
            }
        }

        return true;
    }

    @Override
    public Object provideMessageInfo(int msg, Telegraph receiver) {
        return this;
    }
}
