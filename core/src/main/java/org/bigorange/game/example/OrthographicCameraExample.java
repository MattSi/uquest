package org.bigorange.game.example;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class OrthographicCameraExample implements ApplicationListener {
    static final int WORLD_WIDTH = 256;
    static final int WORLD_HEIGHT = 256;

    static final int VIEW_PORT = 100;

    private OrthographicCamera camera;

    private OrthographicCamera camera2;
    private Viewport viewport;
    private Viewport viewport2;

    private SpriteBatch batch;
    private SpriteBatch batch2;

    private Sprite mapSprite;
    private float rotationSpeed;


    @Override
    public void create() {
        rotationSpeed = 0.5f;
        mapSprite = new Sprite(new Texture(Gdx.files.internal("example/badlogic.jpg")));
        mapSprite.setPosition(0,0);
        mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio
        camera = new OrthographicCamera(VIEW_PORT, VIEW_PORT * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        viewport = new FitViewport(VIEW_PORT, VIEW_PORT*(h/w), camera);

        camera2 = new OrthographicCamera(VIEW_PORT, VIEW_PORT * (h / w));
        camera2.position.set(camera.viewportWidth / 2f + 50, camera.viewportHeight / 2f + 50, 0);
        camera2.update();
        viewport2 = new FitViewport(VIEW_PORT, VIEW_PORT*(h/w), camera);

        batch = new SpriteBatch();
        batch2 = new SpriteBatch();

    }

    @Override
    public void resize(int width, int height) {
       //camera.viewportWidth = VIEW_PORT * 1f;
       //camera.viewportHeight = VIEW_PORT * height/width;
       //camera.update();
       viewport.update(width, height);
       viewport2.update(width, height);
       Gdx.app.log("Example:", ""+ width + ":"+height);
    }

    @Override
    public void render() {
        handleInput();
        camera.update();
        // TODO find out Y
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        mapSprite.draw(batch);
        batch.end();

//        camera2.update();
//        // TODO find out Y
//        batch2.setProjectionMatrix(camera2.combined);
//
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        batch2.begin();
//        mapSprite.draw(batch2);
//        batch2.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        mapSprite.getTexture().dispose();
        batch.dispose();
        batch2.dispose();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.rotate(-rotationSpeed, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(rotationSpeed, 0, 0, 1);
        }

//        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, VIEW_PORT/camera.viewportWidth);
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 10);
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        //camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, WORLD_WIDTH - effectiveViewportWidth / 2f);
        //camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, WORLD_WIDTH - effectiveViewportHeight / 2f);
        Gdx.app.log("Example", ""+camera.zoom);
    }


}
