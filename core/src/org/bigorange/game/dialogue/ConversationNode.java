package org.bigorange.game.dialogue;

/**
 * Conversation is a part of a NPC or an Enemy
 *
 * ConversationNode class is a node of a conversation.
 */
public class ConversationNode {
    private int id;
    private String dialog = "";

    public ConversationNode() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }
}
