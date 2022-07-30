package org.bigorange.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.bigorange.game.Utils;
import org.bigorange.game.dialogue.ConversationGraph;
import org.bigorange.game.dialogue.ConversationGraphObserver;
import org.bigorange.game.ecs.component.ActionableComponent;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.EMouse;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.message.MessageType;
import org.bigorange.game.screens.BaseScreen;
import org.bigorange.game.screens.ScreenManager;

public class PlayerHUD extends BaseScreen implements  ConversationGraphObserver, Telegraph {
    private final Viewport viewport;
    private final Camera camera;

    private final ConversationUI conversationUI;
    private final StatusUI statusUI;

    public PlayerHUD(TTFSkin skin, ScreenManager screenManager) {
        super(skin, screenManager);
        MessageManager.getInstance().addListener(this, MessageType.PLAYER_CLOSE_TO_NPC);
        MessageManager.getInstance().addListener(this, MessageType.MSG_PLAYER_TALK_TO_NPC);
        this.camera = new OrthographicCamera();
        viewport = new ScreenViewport(this.camera);
        stage.setDebugAll(false);


        conversationUI = new ConversationUI(skin);
        conversationUI.setMovable(true);
        conversationUI.setVisible(false);
        conversationUI.setPosition(stage.getWidth() / 2, 0);
        conversationUI.setWidth(stage.getWidth() / 2);
        conversationUI.setHeight(stage.getHeight() / 2);

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
        conversationUI.getCloseButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onConversationUIClosed();
            }
        });
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {
        super.keyDown(manager, key);
        Gdx.app.log(TAG, "" + key.toString() + key.ordinal());
        if (key == EKey.ESCAPE) {
            onConversationUIClosed();
        }
        switch (key) {
            case LEFT -> stage.keyDown(Input.Keys.LEFT);
            case RIGHT -> stage.keyDown(Input.Keys.RIGHT);
            case UP -> stage.keyDown(Input.Keys.UP);
            case DOWN -> stage.keyDown(Input.Keys.DOWN);
            case SELECT -> stage.keyDown(Input.Keys.ENTER);
        }
    }

    @Override
    public void buttonDown(InputManager manager, EMouse mouse, Vector2 screenPoint, int pointer) {
        stage.touchDown((int) screenPoint.x, (int) screenPoint.y, pointer, mouse.getMouseCode());
    }

    @Override
    public void buttonUp(InputManager manager, EMouse mouse, Vector2 screenPoint, int pointer) {
        stage.touchUp((int) screenPoint.x, (int) screenPoint.y, pointer, mouse.getMouseCode());
    }

    @Override
    public void show() {
        Gdx.app.log(TAG, "Show......");
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
        switch (event) {
            case NONE -> {
                Gdx.app.log(TAG, "XXXXXXXXXXXXXXXXXXXXXXXX");
            }
            case EXIT_CONVERSATION -> {
                onConversationUIClosed();
            }
        }
    }

    public Camera getCamera() {
        return camera;
    }


    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {
            case MessageType.PLAYER_CLOSE_TO_NPC -> {
                ActionableComponent action = (ActionableComponent) msg.extraInfo;
                switch (action.type) {
                    case TALK -> {
                        Gdx.app.debug(TAG, "Player HUD received message: PLAYER_CLOSE_TO_NPC");
                    }
                }
            }
            case MessageType.MSG_PLAYER_TALK_TO_NPC -> {
                if(!conversationUI.isVisible()) {
                    ActionableComponent gameObj = (ActionableComponent) msg.extraInfo;
                    conversationUI.loadConversation(gameObj);
                    onConversationUIOpen();
                }
            }
        }
        return true;
    }

    private void onConversationUIClosed() {
        conversationUI.setVisible(false);
        Utils.getInputManager().removeKeyInputListener(this);
        Utils.getInputManager().setEnableKeyInputListeners(true);

        InputMultiplexer inputProcessor = (InputMultiplexer) Gdx.input.getInputProcessor();
        inputProcessor.removeProcessor(stage);
        inputProcessor.addProcessor(Utils.getInputManager());
    }

    private void onConversationUIOpen() {
        conversationUI.setVisible(true);
        Utils.getInputManager().setEnableKeyInputListeners(false);
        Utils.getInputManager().addKeyInputListener(this);
    }
}
