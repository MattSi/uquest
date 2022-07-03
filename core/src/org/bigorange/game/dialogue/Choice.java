package org.bigorange.game.dialogue;

public class Choice {
    private String message;
    private DialogueNode nextNode;

    public Choice(String message, DialogueNode nextNode) {
        this.message = message;
        this.nextNode = nextNode;
    }

    public String getMessage() {
        return message;
    }

    public DialogueNode getNextNode() {
        return nextNode;
    }
}
