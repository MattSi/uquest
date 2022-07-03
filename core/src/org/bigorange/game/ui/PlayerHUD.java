package org.bigorange.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.bigorange.game.core.dialogue.ConversationGraph;
import org.bigorange.game.core.dialogue.ConversationGraphObserver;

public class PlayerHUD implements Screen, ConversationGraphObserver {
    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage stage;
    private Viewport viewport;
    private Camera camera;

    private ConversationUI conversationUI;
    private StatusUI statusUI;

    public PlayerHUD(Camera camera) {
        this.camera = camera;
        viewport = new ScreenViewport(this.camera);
        stage = new Stage(this.viewport);
        stage.setDebugAll(false);


        //TODO FIXME
        TextureAtlas STATUSUI_TEXTUREATLAS = new TextureAtlas("data/uiskin.atlas");
        Skin STATUSUI_SKIN = new Skin(Gdx.files.internal("data/uiskin.json"), STATUSUI_TEXTUREATLAS);

        conversationUI = new ConversationUI(STATUSUI_SKIN);
        conversationUI.setMovable(true);
        conversationUI.setVisible(true);
        conversationUI.setPosition(stage.getWidth() / 2, 0);
        conversationUI.setWidth(stage.getWidth() / 2);
        conversationUI.setHeight(stage.getHeight() / 2);
        conversationUI.loadConversation();

        statusUI = new StatusUI(STATUSUI_SKIN, STATUSUI_TEXTUREATLAS);
        statusUI.setVisible(true);
        statusUI.setPosition(0, stage.getHeight() - statusUI.getHeight());
        statusUI.setKeepWithinStage(false);
        statusUI.setMovable(false);

        //=================================================================
        stage.addActor(conversationUI);
        stage.addActor(statusUI);


        //=================================================================
        conversationUI.validate();
        statusUI.validate();


        //=======================================================================================
        //Listeners
        conversationUI.getCloseButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                conversationUI.setVisible(false);

            }
        });

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void onNotify(ConversationGraph graph, ConversationCommandEvent event) {
        switch (event){
            case NONE -> {
            }
            case EXIT_CONVERSATION -> {
                conversationUI.setVisible(false);
            }
        }
    }
}
