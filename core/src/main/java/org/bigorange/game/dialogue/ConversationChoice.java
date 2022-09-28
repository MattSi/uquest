package org.bigorange.game.dialogue;

/**
 * 对话节点选择项
 *  -- 当对话进行时，玩家会根据NPC的对话，做出一些选择
 *  sourceId : 当前对话节点
 *  destinationId : 要跳转到的对话节点
 *  choicePhrase : 选择的文本
 *  conversationCommandEvent : 选择后需要执行的命令
 */
public class ConversationChoice {
    private int sourceId;
    private int destinationId;
    private String choicePhrase;
    private ConversationGraphObserver.ConversationCommandEvent conversationCommandEvent;

    public ConversationChoice() {
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public String getChoicePhrase() {
        return choicePhrase;
    }

    public void setChoicePhrase(String choicePhrase) {
        this.choicePhrase = choicePhrase;
    }

    public ConversationGraphObserver.ConversationCommandEvent getConversationCommandEvent() {
        return conversationCommandEvent;
    }

    public void setConversationCommandEvent(ConversationGraphObserver.ConversationCommandEvent conversationCommandEvent) {
        this.conversationCommandEvent = conversationCommandEvent;
    }

    @Override
    public String toString() {
        return choicePhrase;
    }
}
