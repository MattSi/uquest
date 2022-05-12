package org.bigorange.game.tests.cases;

import org.bigorange.game.core.dialogue.ConversationManager;
import org.bigorange.game.core.dialogue.TalkNode;
import org.junit.Assert;
import org.junit.Test;

public class ConversationTest {

    @Test
    public void test1(){
        ConversationManager manager = new ConversationManager();

        TalkNode node = manager.getNextTalk(1);
        System.err.println(node.getMessage());

        node = manager.getNextTalk(1);
        System.err.println(node.getMessage());

        node = manager.getNextTalk(1);
        System.err.println(node.getMessage());

        node = manager.getNextTalk(1);
        System.err.println(node.getMessage());

        node = manager.getNextTalk(1);
        System.err.println(node.getMessage());

        node = manager.getNextTalk(1);
        System.err.println(node.getMessage());

        node = manager.getNextTalk(1);
        System.err.println(node.getMessage());
        node = manager.getNextTalk(1);
        System.err.println(node.getMessage());
        node = manager.getNextTalk(1);
        System.err.println(node.getMessage());


        Assert.assertNotNull(node);
    }
}
