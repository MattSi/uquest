package org.bigorange.game.example;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.*;

public class ViewportTest extends InputAdapter implements ApplicationListener {
    Array<Viewport> viewports;
    Array<String> names;
    Stage stage;
    Label label;

    float d = 0f;
    int turnRight = 1;

    public void create () {
        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("example/uiskin.json"));

        label = new Label("", skin);
        Table root = new Table(skin);
        root.setFillParent(true);
        root.setBackground(skin.getDrawable("default-pane"));
        root.add(new TextButton("Button 1", skin));
        root.add(new TextButton("Button 2", skin)).row();
        root.add("Press spacebar to change the viewport:").colspan(2).row();
        root.add(label).colspan(2);
        stage.addActor(root);
        stage.setDebugAll(true);

        viewports = getViewports(stage.getCamera());
        names = getViewportNames();

        stage.setViewport(viewports.first());
        label.setText(names.first());

        Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.SPACE){
                    int i = (viewports.indexOf(stage.getViewport(), true) + 1) % viewports.size;
                    label.setText(names.get(i));
                    Viewport viewport = viewports.get(i);
                    stage.setViewport(viewport);
                    resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                }
                return false;
            }
        }, stage));

    }

    public void resume () {
    }

    public void render () {
        stage.act();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(d < Gdx.graphics.getWidth()/2) {
            d += Gdx.graphics.getDeltaTime() *100 * turnRight;
        } else {
            d= Gdx.graphics.getWidth()/2;
        }
        Gdx.gl.glViewport(Math.round(d), 0, Gdx.graphics.getWidth() - Math.round(d), Gdx.graphics.getHeight());
        stage.draw();
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height);
    }

    public void pause () {
    }

    public void dispose () {
        stage.dispose();
    }
    static public Array<String> getViewportNames () {
        Array<String> names = new Array();
        names.add("StretchViewport");
        names.add("FillViewport");
        names.add("FitViewport");
        names.add("ExtendViewport: no max");
        names.add("ExtendViewport: max");
        names.add("ScreenViewport: 1:1");
        names.add("ScreenViewport: 0.75:1");
        names.add("ScalingViewport: none");
        return names;
    }

    static public Array<Viewport> getViewports(Camera camera){
        int minWorldWidth = 640;
        int minWorldHeight = 480;
        int maxWorldWidth = 800;
        int maxWorldHeight = 600;

        Array<Viewport> viewports = new Array<>();
        viewports.add(new StretchViewport(minWorldWidth, minWorldHeight, camera));
        viewports.add(new FillViewport(minWorldWidth, minWorldHeight, camera));
        viewports.add(new FitViewport(minWorldWidth, minWorldHeight, camera));
        viewports.add(new ExtendViewport(minWorldWidth, minWorldHeight, camera));
        viewports.add(new ExtendViewport(minWorldWidth, minWorldHeight, maxWorldWidth, maxWorldHeight, camera));
        viewports.add(new ScreenViewport(camera));

        ScreenViewport screenViewport = new ScreenViewport(camera);
        screenViewport.setUnitsPerPixel(0.75f);
        viewports.add(screenViewport);

        viewports.add(new ScalingViewport(Scaling.none, minWorldWidth, minWorldHeight, camera));
        return viewports;
    }
}
