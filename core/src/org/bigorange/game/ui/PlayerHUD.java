package org.bigorange.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
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
import org.bigorange.game.ecs.component.ActionableComponent;
import org.bigorange.game.ecs.component.GameObjectComponent2;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.EMouse;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.input.MouseInputListener;
import org.bigorange.game.message.MessageType;
import org.bigorange.game.screens.BaseScreen;
import org.bigorange.game.screens.ScreenManager;

public class PlayerHUD extends BaseScreen implements  ConversationGraphObserver, Telegraph {
    private Viewport viewport;
    private Camera camera;

    private ConversationUI conversationUI;
    private StatusUI statusUI;

    public PlayerHUD(TTFSkin skin, ScreenManager screenManager) {
        super(skin, screenManager);
        MessageManager.getInstance().addListener(this, MessageType.PLAYER_CLOSE_TO_NPC);
        MessageManager.getInstance().addListener(this, MessageType.MSG_PLAYER_TALK_TO_NPC);
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
            }
        });
        conversationUI.getCloseButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Gdx.app.log(TAG, "YYYYYYYYYYYYYYYYYYYYYYYYYYY");

            }
        });
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {
        super.keyDown(manager, key);
        if(key == EKey.ESCAPE){
            conversationUI.setVisible(false);

            InputMultiplexer inputProcessor =(InputMultiplexer) Gdx.input.getInputProcessor();
            inputProcessor.removeProcessor(stage);
            inputProcessor.addProcessor(Utils.getInputManager());
        }
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


    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message){
            case MessageType.PLAYER_CLOSE_TO_NPC -> {
                ActionableComponent action = (ActionableComponent) msg.extraInfo;
                switch (action.type){
                    case TALK -> {
                        Gdx.app.debug(TAG, "Player HUD received message: PLAYER_CLOSE_TO_NPC");
                    }
                }
            }
            case MessageType.MSG_PLAYER_TALK_TO_NPC -> {
                GameObjectComponent2 gameObjCmp = (GameObjectComponent2) msg.extraInfo;
                InputMultiplexer inputProcessor =(InputMultiplexer) Gdx.input.getInputProcessor();

                inputProcessor.removeProcessor(Utils.getInputManager());
                inputProcessor.addProcessor(stage);
                conversationUI.setVisible(true);
            }
        }
        return true;
    }
}
