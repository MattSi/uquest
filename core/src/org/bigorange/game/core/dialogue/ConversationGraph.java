package org.bigorange.game.core.dialogue;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;


public class ConversationGraph extends ConversationGraphSubject {
    private IntMap<ConversationNode> conversations;
    private IntMap<Array<ConversationChoice>> associatedChoices;

    private int currentConversationId = 0;

    public ConversationGraph() {
    }

    public ConversationGraph(IntMap<ConversationNode> conversations, int rootId){
        setConversations(conversations);
        setCurrentConversationNode(rootId);
    }

    public void setConversations(IntMap<ConversationNode> conversations){
        if(conversations.size < 0){
            throw new IllegalArgumentException("Can't have a negative amount of conversations");
        }

        this.conversations = conversations;
        this.associatedChoices = new IntMap<>(conversations.size);

        for (ConversationNode node : conversations.values()) {
            associatedChoices.put(node.getId(), new Array<>());
        }
    }


    public ConversationNode getConversationNodeById(int id){
        if(!isValid(id)){
            return null;
        }

        return conversations.get(id);
    }

    public void setCurrentConversationNode(int id) {
        final ConversationNode conversationNode = getConversationNodeById(id);
        if (conversationNode == null) return;

        if (currentConversationId == 0 ||
                currentConversationId == id ||
                isReachable(currentConversationId, id)) {
            currentConversationId = id;
        }
    }

    public Array<ConversationChoice> getCurrentChoices(){
        return associatedChoices.get(currentConversationId);
    }

    public boolean isValid(int conversationId){
        final ConversationNode conversationNode = conversations.get(conversationId);
        if(conversationNode == null) return false;

        return true;
    }

    public boolean isReachable(int sourceId, int sinkId){
        if(!isValid(sourceId) || !isValid(sinkId)) return false;
        if(conversations.get(sourceId) == null) return false;

        final Array<ConversationChoice> list = associatedChoices.get(sourceId);
        if(list == null) return false;
        for (ConversationChoice choice : list) {
            if( choice.getSourceId() == sourceId  &&
                choice.getDestinationId() == sinkId){
                return true;
            }
        }

        return false;
    }

    public void addChoice(ConversationChoice choice){
        Array<ConversationChoice> list = associatedChoices.get(choice.getSourceId());
        if(list == null) return;

        list.add(choice);
    }

    public String toJson(){
        Json json = new Json();
        return json.prettyPrint(this);
    }

    public String toString(){
        StringBuffer output = new StringBuffer();
        int numberTotalChoices = 0;

        final IntMap.Keys keys = associatedChoices.keys();


        return output.toString();
    }
}
