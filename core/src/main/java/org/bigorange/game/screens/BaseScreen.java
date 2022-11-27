package org.bigorange.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.bigorange.game.Utils;
import org.bigorange.game.input.*;
import org.bigorange.game.ui.TTFSkin;

public abstract class BaseScreen implements Screen, InputListener {
    protected Stage stage;
    protected TTFSkin skin;
    protected ScreenManager screenManager;
    protected String TAG = this.getClass().getSimpleName();
    protected boolean keyInputListenerEnabled;

    public BaseScreen(TTFSkin skin, ScreenManager screenManager) {
        this.stage = new Stage(new ScreenViewport(
                new OrthographicCamera()));
        this.skin = skin;
        this.screenManager = screenManager;
        this.keyInputListenerEnabled = true;
    }

    @Override
    public void show() {
        Utils.getInputManager().addKeyInputListener(this);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().apply();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Utils.getInputManager().removeKeyInputListener(this);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {
    }

    @Override
    public void keyUP(InputManager manager, EKey key) {

    }

    @Override
    public void buttonDown(InputManager manager, EMouse mouse, Vector2 screenPoint, int pointer) {
    }

    @Override
    public void buttonUp(InputManager manager, EMouse mouse, Vector2 screenPoint, int pointer) {

    }

    @Override
    public void mouseMoved(InputManager manager, Vector2 screenPoint) {

    }

    @Override
    public void setEnabled(boolean enabled) {
        keyInputListenerEnabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return keyInputListenerEnabled;
    }
}
