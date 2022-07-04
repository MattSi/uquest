package org.bigorange.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.bigorange.game.Utils;
import org.bigorange.game.dialogue.ConversationGraph;
import org.bigorange.game.dialogue.ConversationGraphObserver;
import org.bigorange.game.input.EMouse;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.input.MouseInputListener;
import org.bigorange.game.screens.BaseScreen;
import org.bigorange.game.screens.ScreenManager;

public class PlayerHUD extends BaseScreen implements  ConversationGraphObserver {
    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Viewport viewport;
    private Camera camera;

    private ConversationUI conversationUI;
    private StatusUI statusUI;

    public PlayerHUD(TTFSkin skin, ScreenManager screenManager) {
        super(skin, screenManager);
        this.camera = new OrthographicCamera();
        viewport = new ScreenViewport(this.camera);
        stage.setDebugAll(false);

        InputMultiplexer inputProcessor =(InputMultiplexer) Gdx.input.getInputProcessor();

        inputProcessor.removeProcessor(Utils.getInputManager());
        inputProcessor.addProcessor(stage);


        conversationUI = new ConversationUI(skin);
        conversationUI.setMovable(true);
        conversationUI.setVisible(true);
        conversationUI.setPosition(stage.getWidth() / 2, 0);
        conversationUI.setWidth(stage.getWidth() / 2);
        conversationUI.setHeight(stage.getHeight() / 2);
        conversationUI.loadConversation();

        final TextureAtlas textureAtlas = Utils.getResourceManager().get("hud/uiskin.atlas", TextureAtlas.class);
        statusUI = new StatusUI(skin, textureAtlas);
        statusUI.setVisible(true);
        statusUI.setPosition(0, stage.getHeight() - statusUI.getHeight());
        //statusUI.setKeepWithinStage(false);
        //statusUI.setMovable(false);

        //=================================================================
        stage.addActor(conversationUI);
        stage.addActor(statusUI);


        //=================================================================
        conversationUI.validate();
        statusUI.validate();


        //=======================================================================================
        //Listeners
        conversationUI.getCloseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                conversationUI.setVisible(false);

                InputMultiplexer inputProcessor =(InputMultiplexer) Gdx.input.getInputProcessor();
                inputProcessor.removeProcessor(stage);
                inputProcessor.addProcessor(Utils.getInputManager());
                Button button = (Button) actor;
                Gdx.app.log(TAG, ""+button.isChecked());
            }
        });
        conversationUI.getCloseButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {


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

    public Camera getCamera() {
        return camera;
    }


}
