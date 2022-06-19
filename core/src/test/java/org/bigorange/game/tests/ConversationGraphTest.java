package org.bigorange.game.tests;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import org.bigorange.game.core.dialogue.ConversationChoice;
import org.bigorange.game.core.dialogue.ConversationGraph;
import org.bigorange.game.core.dialogue.ConversationNode;

public class ConversationGraphTest {
    static ConversationGraph graph;
    static IntMap<ConversationNode> conversations;

    static String quit = "q";
    static String input = "";

    public static void main(String[] args) {
        conversations = new IntMap<>();

        ConversationNode start = new ConversationNode();
        start.setId(1);
        start.setDialog("Do you want to play a game?");

        ConversationNode yesAnswer = new ConversationNode();
        yesAnswer.setId(101);
        yesAnswer.setDialog("BOOM! Bombs dropping everywhere");

        ConversationNode noAnswer = new ConversationNode();
        noAnswer.setId(201);
        noAnswer.setDialog("Too bad!");

        ConversationNode unconnectedTest = new ConversationNode();
        unconnectedTest.setId(301);
        unconnectedTest.setDialog("I am unconnected");

        conversations.put(start.getId(), start);
        conversations.put(yesAnswer.getId(), yesAnswer);
        conversations.put(noAnswer.getId(), noAnswer);
        conversations.put(unconnectedTest.getId(), unconnectedTest);

        graph = new ConversationGraph(conversations, start.getId());

        final ConversationChoice yesChoice = new ConversationChoice();
        yesChoice.setSourceId(start.getId());
        yesChoice.setDestinationId(yesAnswer.getId());
        yesChoice.setChoicePhrase("YES");


        final ConversationChoice noChoice = new ConversationChoice();
        noChoice.setSourceId(start.getId());
        noChoice.setDestinationId(noAnswer.getId());
        noChoice.setChoicePhrase("NO");

        final ConversationChoice startChoice01 = new ConversationChoice();
        startChoice01.setSourceId(yesAnswer.getId());
        startChoice01.setDestinationId(start.getId());
        startChoice01.setChoicePhrase("Go to beginning!");

        final ConversationChoice startChoice02 = new ConversationChoice();
        startChoice02.setSourceId(noAnswer.getId());
        startChoice02.setDestinationId(start.getId());
        startChoice02.setChoicePhrase("Go to beginning!");

        graph.addChoice(yesChoice);
        graph.addChoice(noChoice);
        graph.addChoice(startChoice01);
        graph.addChoice(startChoice02);

       // graph.setConversations(conversations);

        System.out.println(graph.toJson());


//        while (!input.equals(quit)){
//            final ConversationNode node = getNextChoice();
//            if(node == null) continue;
//            graph.setCurrentConversationNode(node.getId());
//        }
    }

    public static ConversationNode getNextChoice(){
        final Array<ConversationChoice> choices = graph.getCurrentChoices();

        for (ConversationChoice choice : choices) {
            // ------ Print Log
        }

        input = System.console().readLine();

        ConversationNode choice = null;
        choice = graph.getConversationNodeById(Integer.getInteger(input));

        return choice;
    }
}
