package org.bigorange.game.core.dialogue;

public interface ConversationGraphObserver {

    public static enum ConversationCommandEvent{
        EXIT_CONVERSATION,
        NONE
    }

    void onNotify(final ConversationGraph graph, ConversationCommandEvent event);
}
