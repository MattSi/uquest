package org.bigorange.game.core.dialogue;

import java.util.List;

public class DialogueNode {
    private final int conversationId;
    private final int dialogueNodeId;
    private final NodeType nodeType;
    private String person;
    private String messageId;
    private DialogueNode nextNode;

    private List<Choice> choice;

    public DialogueNode(int conversationId, int dialogueNodeId, NodeType nodeType){
        this.conversationId = conversationId;
        this.dialogueNodeId = dialogueNodeId;
        this.nodeType = nodeType;
        choice = null;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getPerson() {
        return person;
    }

    public DialogueNode getNextNode() {
        return nextNode;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setNextNode(DialogueNode nextNode) {
        this.nextNode = nextNode;
    }

    public List<Choice> getChoice() {
        return choice;
    }

    public int getConversationId() {
        return conversationId;
    }

    public int getDialogueNodeId() {
        return dialogueNodeId;
    }

    public void setChoice(List<Choice> choice) {
        this.choice = choice;
    }

    @Override
    public String toString() {
        return "DialogueNode{" +
                "conversationId=" + conversationId +
                ", dialogueNodeId=" + dialogueNodeId +
                ", nodeType=" + nodeType +
                ", person='" + person + '\'' +
                ", messageId='" + messageId + '\'' +
                ", nextNode=" + nextNode +
                ", choice=" + choice +
                '}';
    }
}
