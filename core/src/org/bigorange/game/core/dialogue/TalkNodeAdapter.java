package org.bigorange.game.core.dialogue;

public class TalkNodeAdapter implements TalkNode {
    protected int conversationId;
    protected NodeType nodeType;
    protected String message;
    protected TalkNode nextNode;
    protected TalkNode[] choose;

    public TalkNodeAdapter(){
        nodeType = NodeType.UNDEFINED;
        message = null;
        nextNode = null;
        choose = null;
        conversationId = -1;
    }
    @Override
    public NodeType getNodeType() {
        return nodeType;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public TalkNode[] getChoose() {
        return choose;
    }

    @Override
    public int getConversationId() {
        return conversationId;
    }

    @Override
    public TalkNode getNextNode() {
        return nextNode;
    }

    void setNextNode(TalkNode nextNode){
        this.nextNode = nextNode;
    }
}
