package org.bigorange.game.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.bigorange.game.core.Utils;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.input.KeyInputListener;
import org.bigorange.game.ui.TTFSkin;

public class BaseScreen implements Screen, KeyInputListener {
    protected Stage stage;
    protected TTFSkin skin;
    protected String TAG = this.getClass().getSimpleName();

    public BaseScreen(TTFSkin skin){
        this.stage = new Stage(new FitViewport( Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                new OrthographicCamera()));
        this.skin = skin;
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
}
