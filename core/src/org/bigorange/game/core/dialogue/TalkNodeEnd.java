package org.bigorange.game.core.dialogue;

public class TalkNodeEnd extends TalkNodeAdapter {
    public TalkNodeEnd(int conversationId) {
        super();

        nodeType = NodeType.END;
        this.conversationId = conversationId;
    }
}
