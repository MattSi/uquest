package org.bigorange.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import org.bigorange.game.dialogue.DialogueNode;

public class DialogueBox extends TextButton {
    private DialogueNode dialogueNode;
    public DialogueBox(String text, Skin skin, String styleName) {
        super(text, skin, styleName);

        dialogueNode = null;
    }

    public DialogueNode getDialogueNode() {
        return dialogueNode;
    }

    public void setDialogueNode(DialogueNode dialogueNode) {
        this.dialogueNode = dialogueNode;
    }
}
