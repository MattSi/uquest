package org.bigorange.game.dialogue;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DialogueTree {
    private int conversationId;

    private HashMap<Integer, DialogueNode> map;

    private DialogueNode start;

    // a line means a dialogue node
    private final int lineNumber;
    private final String[] lines;

    public DialogueTree(int conversationId, int lineNumber, String[] lines) {
        this.conversationId = conversationId;
        this.lineNumber = lineNumber;
        this.lines = lines;
        map = new HashMap<>();
        start = null;

    }

    public void build() {
        /**
         * 1. 第一阶段，建立对话节点
         *
         * 2. 第二阶段，对于每个节点，根据要去填写下一个对话节点地址
         */
        map.clear();
        start = null;
        String csvSplitter = ",";
        // 第一阶段
        for (int i = 0; i < lineNumber; i++) {
            final String[] split = lines[i].split(csvSplitter);
            createDialogueNode(split);
        }


        for (int i = 0; i < lineNumber; i++) {
            final String[] split = lines[i].split(csvSplitter);
            final DialogueNode dialogueNode = getDialogueNode(split);
            if(dialogueNode.getNodeType() == NodeType.START){
                start = dialogueNode;
            }
        }
    }

    private DialogueNode getDialogueNode(String[] item){
        int nodeId = Integer.valueOf(item[0]);
        int nodeType = Integer.valueOf(item[2]);


        final DialogueNode dialogueNode = map.get(nodeId);
        switch (dialogueNode.getNodeType()){
            case START -> {
                final Integer nextNodeId = Integer.valueOf(item[4]);
                final DialogueNode nextNode = map.get(nextNodeId);
                dialogueNode.setNextNode(nextNode);
            }

            case MESSAGE -> {
                final String message = item[3];
                final Integer nextNodeId = Integer.valueOf(item[4]);
                final DialogueNode nextNode = map.get(nextNodeId);
                dialogueNode.setMessageId(message);
                dialogueNode.setNextNode(nextNode);
            }

            case CHOICE -> {
                List<Choice> choiceList = new ArrayList<>();
                final String chooseMessage1 = item[3];
                final Integer chooseNextNodeId1 = Integer.valueOf(item[4]);
                final String chooseMessage2 = item[5];
                final Integer chooseNextNodeId2 = Integer.valueOf(item[6]);

                final DialogueNode nextNode1 = map.get(chooseNextNodeId1);
                choiceList.add(new Choice(chooseMessage1, nextNode1));

                final DialogueNode nextNode2 = map.get(chooseNextNodeId2);
                choiceList.add(new Choice(chooseMessage2, nextNode2));
                dialogueNode.setChoice(choiceList);
            }

            case END -> {
                ;
            }

            case UNDEFINED -> {
                throw new UndergroundQuestDialogueException("Undefined Dialogue Node" + dialogueNode.toString());
            }
        }

        return dialogueNode;
    }

    private DialogueNode createDialogueNode(String[] item) {
        int nodeId = Integer.valueOf(item[0]);
        int nodeType = Integer.valueOf(item[2]);
        String person = item[1];

        DialogueNode node = _createDialogueNode(nodeId, nodeType);
        node.setPerson(person);

        return node;
    }

    private DialogueNode _createDialogueNode(int nodeId, int nodeType) {
        final DialogueNode dialogueNode = new DialogueNode(conversationId, nodeId, NodeType.getNodeTypeByCode(nodeType));
        map.put(nodeId, dialogueNode);
        return dialogueNode;
    }

    public DialogueNode getStart() {
        return start;
    }
}
