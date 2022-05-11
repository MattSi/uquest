package org.bigorange.game.core.dialogue;

public class TalkNodeStart extends TalkNodeAdapter{

    public TalkNodeStart(int conversationId){
        super();

        nodeType = NodeType.START;
        this.conversationId = conversationId;
    }
}
