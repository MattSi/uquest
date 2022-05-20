package org.bigorange.game.tests.cases;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import org.bigorange.game.core.dialogue.ConversationManager;
import org.bigorange.game.core.dialogue.DialogueNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConversationTest {

    private ConversationManager conversationManager;

    @Before
    public void init(){
        FileHandle handle = new FileHandle("C:/Oracle/projs/game/libgdx/uquest/android/assets/dialogue/conversations.csv");
        conversationManager = new ConversationManager(handle);
    }

    @Test
    public void test1(){

        final DialogueNode talk1 = conversationManager.talk(1);
        System.err.println(talk1.getMessageId());
        final DialogueNode talk2 = conversationManager.talk(1);
        System.err.println(talk2.getMessageId());
        Assert.assertNotNull(talk1);
    }
}
