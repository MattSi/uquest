package org.bigorange.game.core.dialogue;

import com.badlogic.gdx.utils.I18NBundle;
import org.bigorange.game.core.ResourceManager;

import java.util.Locale;

public class ConversationManager implements ResourceManager.LocaleListener {
    private I18NBundle i18NBundle;

    public ConversationManager() {

    }

    private void buildConversationTree(Locale locale) {

    }

    public TalkNode getNextTalk(int conversationId) {
        return null;
    }

    @Override
    public void localeChanged(Locale locale) {
        buildConversationTree(locale);
    }
}
