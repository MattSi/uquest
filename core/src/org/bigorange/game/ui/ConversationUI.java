package org.bigorange.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import org.bigorange.game.dialogue.ConversationChoice;
import org.bigorange.game.dialogue.ConversationGraph;
import org.bigorange.game.dialogue.ConversationNode;

/**
 * Conversation UI: embedded in Player HUD
 */
public class ConversationUI extends Window {
    public static final String TAG = ConversationUI.class.getSimpleName();

    private Label dialogText;
    private List listItems;
    private ConversationGraph graph;
    private TextButton closeButton;
    private Json json;





    public ConversationUI(Skin skin) {
        super("dialog", skin, "default");

        json = new Json();
        graph = new ConversationGraph();

        //create
        dialogText = new Label("No Conversation", skin, "info");
        dialogText.setWrap(true);
        dialogText.setAlignment(Align.center);
        listItems = new List<ConversationChoice>(skin);

        closeButton = new TextButton("X", skin);

        ScrollPane scrollPane = new ScrollPane(listItems, skin, "default");
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(true, false);
        scrollPane.setScrollBarPositions(false, true);

        //layout
        this.add();
        this.add(closeButton);
        this.row();

        this.defaults().expand().fill();
        this.add(dialogText).pad(10, 10, 10, 10);
        this.row();
        this.add(scrollPane).pad(10, 10, 10, 10);

        //this.debug();
        this.pack();

        //listeners
        listItems.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            }
        });
//        listItems.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                Gdx.app.log(TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//                final ConversationChoice choice = (ConversationChoice) listItems.getSelected();
//                if (choice == null) return;
//                graph.notify(graph, choice.getConversationCommandEvent());
//                populateConversationDialog(choice.getDestinationId());
//            }
//        });

    }

    //TODO, hard code for now, how to locate NPC's conversation ID
    public void loadConversation() {
        this.getTitleLabel().setText("");

        clearDialog();

        final ConversationGraph g = json.fromJson(ConversationGraph.class, Gdx.files.internal("conversations/conversation001.json"));
        setConversationGraph(g);
    }

    public void setConversationGraph(ConversationGraph g) {
        if (graph != null) graph.removeAllObservers();
        this.graph = g;
        populateConversationDialog(graph.getCurrentConversationId());
    }

    public TextButton getCloseButton() {
        return closeButton;
    }

    private void populateConversationDialog(int conversationId) {
        clearDialog();

        final ConversationNode cNode = graph.getConversationNodeById(conversationId);
        if (cNode == null) return;
        graph.setCurrentConversationNode(conversationId);
        dialogText.setText(cNode.getDialog());
        final Array<ConversationChoice> choices = graph.getCurrentChoices();
        if (choices == null) return;
        listItems.setItems(choices);
        listItems.setSelectedIndex(-1);
    }

    private void clearDialog() {
        dialogText.setText("");
        listItems.clearItems();
    }


}
