package org.bigorange.game.dialogue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.IntMap;
import org.bigorange.game.message.MessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理各种对话
 */
public class ConversationManager implements Telegraph, TelegramProvider {
    public static final String TAG = ConversationManager.class.getSimpleName();
    private int currentConversationId;
    private DialogueNode currentDialogueNode;

    private IntMap<DialogueTree> dialogueTrees;

    private FileHandle convHandler;
    public ConversationManager(FileHandle convHandler) {
        currentConversationId = -1;
        currentDialogueNode = null;
        dialogueTrees = new IntMap<>();
        MessageManager.getInstance().addProvider(this, MessageType.MSG_NPC_TALK_TO_PLAYER);
        MessageManager.getInstance().addListener(this, MessageType.PLAYER_AWAY_FROM_NPC);
        MessageManager.getInstance().addListener(this, MessageType.MSG_PLAYER_TALK_TO_NPC);

        buildConversationTrees(convHandler);
    }


    //
    public DialogueNode talk(int conversationId) {
        /**
         * 根据对话ID，以及当前的对话节点，找出下一个对话节点
         *
         */
        DialogueNode result = null;
        if (conversationId != currentConversationId || currentDialogueNode == null) {

            final DialogueTree dialogueTree = dialogueTrees.get(conversationId);
            if(dialogueTree == null){
                Gdx.app.error(TAG, "No conversation according to this id: " + conversationId);
                return null;
            }
            currentDialogueNode = dialogueTree.getStart();
            currentConversationId = conversationId;

        }

        if(currentDialogueNode == null){
            //
            return null;
        } else {
            switch (currentDialogueNode.getNodeType()){
                case END -> {
                    result = currentDialogueNode;
                    currentConversationId = -1;
                    currentDialogueNode = null;
                    return result;
                }
                case START -> {
                    currentDialogueNode = currentDialogueNode.getNextNode();
                }
                case UNDEFINED -> {
                    throw new UndergroundQuestDialogueException("Undefined Dialogue Node" + currentDialogueNode.toString());
                }
            }
        }
        result = currentDialogueNode;
        currentDialogueNode = currentDialogueNode.getNextNode();
        return result;
    }


    private void buildConversationTrees(FileHandle convHandle){
        /**
         * 构建对话树
         * 对话树文本结构
         * conversationId, lineNumber = n
         * line1 -> DialogueNodeId,Person,DialogueNodeType, [[MessageId, NextNode],…]
         * line2
         * ... ...
         * linen
         *
         * conversatonId = -1 means over.
         */
        //final FileHandle convHandle = Gdx.files.internal("dialogue/conversations.csv");
        final String text = convHandle.readString("utf-8");
        final String[] lines = text.split("\\r?\\n");
        int i = 0;
        while (true) {
            final String[] split = lines[i++].split(",");
            int conversationId = Integer.valueOf(split[0]);
            if (conversationId == -1) {
                break;
            }

            int lineNumber = Integer.valueOf(split[1]);
            List<String> conversation = new ArrayList<>();
            for (int j = 0; j < lineNumber; j++, i++) {
                conversation.add(lines[i]);
            }
            final DialogueTree dialogueTree = new DialogueTree(conversationId, lineNumber, conversation.toArray(new String[0]));
            dialogueTree.build();
            dialogueTrees.put(conversationId, dialogueTree);
        }
    }


    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message){
            case MessageType.PLAYER_AWAY_FROM_NPC -> {
                currentDialogueNode = null;
                currentConversationId = -1;
                return true;
            }

            case MessageType.MSG_PLAYER_TALK_TO_NPC -> {
//                final DialogueNode node = (DialogueNode)msg.extraInfo;
//                if(node.getConversationId() != currentConversationId){
//                    Gdx.app.error(TAG, "Conversation does not match: [" + node.getConversationId()
//                            + ", " + currentConversationId +"]");
//                    return false;
//                }
//
//                currentDialogueNode = node;
//                final DialogueNode talkNode = talk(node.getConversationId());
//                MessageManager.getInstance().dispatchMessage(0.2f, this, MessageType.MSG_NPC_TALK_TO_PLAYER, talkNode);
            }

        }
        return false;
//        if (msg.message == MessageType.MSG_PLAYER_LEAVE_NPC) {
//            currentDialogueNode = null;
//            currentConversationId = -1;
//            return true;
//        } else {
//            return false;
//        }
    }

    @Override
    public Object provideMessageInfo(int msg, Telegraph receiver) {
        return null;
    }
}
