package org.bigorange.game.core.dialogue;

public class TalkNodeChoose extends TalkNodeAdapter{
    public TalkNodeChoose(int conversationId){
        super();

        nodeType = NodeType.CHOOSE;
        this.conversationId = conversationId;
    }
}
