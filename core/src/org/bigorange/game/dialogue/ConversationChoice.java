package org.bigorange.game.dialogue;

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
