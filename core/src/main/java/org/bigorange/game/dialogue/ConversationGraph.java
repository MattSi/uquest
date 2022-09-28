package org.bigorange.game.dialogue;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;

/**
 * 对话图 : 玩家和NPC对话时，所有对话节点和选择节点的数据结构
 *
 * conversations : 对话节点(node)集合
 * associatedChoices : 每个对话节点的关联选择项
 */
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
        if(isInvalid(id)){
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

    public boolean isInvalid(int conversationId){
        final ConversationNode conversationNode = conversations.get(conversationId);
        return conversationNode == null;
    }

    public boolean isReachable(int sourceId, int sinkId){
        if(isInvalid(sourceId) || isInvalid(sinkId)) return false;
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

    public int getCurrentConversationId() {
        return currentConversationId;
    }

    public void setCurrentConversationId(int currentConversationId) {
        this.currentConversationId = currentConversationId;
    }

    public String toJson(){
        Json json = new Json();
        return json.prettyPrint(this);
    }

    @SuppressWarnings("DefaultLocale")
    public String toString(){
        StringBuilder outputString = new StringBuilder();
        int numberTotalChoices = 0;

        final IntMap.Keys keys = associatedChoices.keys();
        while(keys.hasNext){
            final int id = keys.next();

            outputString.append(String.format("[%s]: ", id));

            for (ConversationChoice choice : associatedChoices.get(id)) {
                numberTotalChoices++;
                outputString.append(String.format("%s ", choice.getDestinationId()));
            }

            outputString.append(System.getProperty("line.separator"));
        }

        outputString.append(String.format("Number conversations: %d", conversations.size));
        outputString.append(String.format(", Number of choices: %d", numberTotalChoices));


        return outputString.toString();
    }
}
