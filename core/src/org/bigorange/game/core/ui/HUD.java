package org.bigorange.game.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.core.Utils;
import org.bigorange.game.core.input.EKey;
import org.bigorange.game.core.input.InputManager;
import org.bigorange.game.core.input.KeyInputListener;

public class HUD extends InputListener implements Disposable, KeyInputListener {
    private static final String TAG = HUD.class.getSimpleName();

    private final Stage stage;
    private final TTFSkin skin;
    private final I18NBundle i18NBundle;
    private final Stack gameStateHUDs;

    public HUD() {
        stage = new Stage(
                new FitViewport(1280,720,new OrthographicCamera()),
                Utils.getSpriteBatch());
        gameStateHUDs = new Stack();
        gameStateHUDs.setFillParent(true);
        this.stage.addActor(gameStateHUDs);

        final ResourceManager resourceManager = Utils.getResourceManager();

        // TODO: Add I18NBundle and TTFSkin
        i18NBundle = null;
        skin = null;

    }

    public Stage getStage() {
        return stage;
    }

    public TTFSkin getSkin() {
        return skin;
    }

    public void step(final float deltaTime){
        stage.act(deltaTime);
    }

    public void render(final float deltaTime){
        stage.act(deltaTime);
        stage.getViewport().apply();
        stage.draw();
    }

    public void resize(final int width, final int height) {
        Gdx.app.debug(TAG, "Resize HUD to " + width + "x" + height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {

    }

    @Override
    public void keyUP(InputManager manager, EKey key) {

    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return super.touchDown(event, x, y, pointer, button);
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "Disposing HUD");
        stage.dispose();
    }
}
