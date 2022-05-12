package org.bigorange.game.core.dialogue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectSet;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.message.MessageType;

import java.util.Locale;

public class ConversationManager implements ResourceManager.LocaleListener, Telegraph {
    public static final String TAG = ConversationManager.class.getSimpleName();
    private int currentConversationId;
    private TalkNode currentTalkNode;
    private IntMap<TalkNode> conversations;

    public ConversationManager() {
        currentConversationId = -1;
        currentTalkNode = null;
        conversations = new IntMap<>();

        buildConversationTree(null);
        MessageManager.getInstance().addListener(this, MessageType.MSG_PLAYER_LEAVE_NPC);

    }


    //
    public TalkNode getNextTalk(int conversationId) {
        /**
         * 根据对话ID，以及当前的对话节点，找出下一个对话节点
         *
         */
        if (conversationId != currentConversationId
                || currentTalkNode == null
                || currentTalkNode.getNodeType() == TalkNode.NodeType.END) {
            //
            TalkNode talkNode = conversations.get(conversationId);
            if (talkNode == null) {
                // Gdx.app.error(TAG, "No conversation to this id: " + conversationId);
                return null;
            }

            currentConversationId = conversationId;
            currentTalkNode = talkNode;
        }

        if (currentTalkNode.getNodeType() != TalkNode.NodeType.END) {
            currentTalkNode = currentTalkNode.getNextNode();
        } else {
            currentConversationId = -1;
            currentTalkNode = null;
        }

        return currentTalkNode;
    }

    @Override
    public void localeChanged(Locale locale) {
        buildConversationTree(locale);
    }

    private void buildConversationTree(Locale locale) {
        int cId = 1;
        TalkNodeAdapter start = new TalkNodeStart(cId);

        TalkNodeAdapter talk1 = new TalkNodeTalk(cId, "你好，我是老史");
        TalkNodeAdapter talk2 = new TalkNodeTalk(cId, "我不爱上课");
        TalkNodeAdapter talk3 = new TalkNodeTalk(cId, "帮我点个名");

        TalkNodeAdapter end = new TalkNodeEnd(cId);


        start.setNextNode(talk1);
        talk1.setNextNode(talk2);
        talk2.setNextNode(talk3);
        talk3.setNextNode(end);

        ObjectSet<TalkNode> talkSet = new ObjectSet<>();
        talkSet.add(start);
        talkSet.add(talk1);
        talkSet.add(talk2);
        talkSet.add(talk3);
        talkSet.add(end);

        conversations.put(cId, start);

    }

    @Override
    public boolean handleMessage(Telegram msg) {
        currentTalkNode = null;
        currentConversationId = -1;
        return true;
    }
}
