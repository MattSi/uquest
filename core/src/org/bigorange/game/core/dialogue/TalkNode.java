package org.bigorange.game.core.dialogue;


public interface TalkNode {

    NodeType getNodeType();
    String getMessage();
    TalkNode[] getChoose();
    int getConversationId();
    TalkNode getNextNode();


    public enum NodeType {
        START,
        END,
        TALK,
        CHOOSE,
        UNDEFINED
    }
}
