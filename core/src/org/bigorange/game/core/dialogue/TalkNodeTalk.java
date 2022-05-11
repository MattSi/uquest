package org.bigorange.game.core.dialogue;

public class TalkNodeTalk extends TalkNodeAdapter{

    public TalkNodeTalk(int conversationId, String message){
        super();

        nodeType = NodeType.TALK;
        this.conversationId = conversationId;
        this.message = message;
    }
}
