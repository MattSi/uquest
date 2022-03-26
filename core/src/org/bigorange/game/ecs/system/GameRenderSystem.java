package org.bigorange.game.ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.bigorange.game.ResourceManager;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.input.KeyInputListener;
import org.bigorange.game.utils.Utils;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.RenderSystem;
import org.bigorange.game.map.Map;
import org.bigorange.game.map.MapListener;

import static org.bigorange.game.UndergroundQuest.UNIT_SCALE;

public class GameRenderSystem implements RenderSystem, MapListener, KeyInputListener {
    private static final String TAG = GameRenderSystem.class.getSimpleName();


    private final Viewport viewport;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private Array<TiledMapTileLayer> layersToRender;
    private final SpriteBatch spriteBatch;
    private final OrthographicCamera gameCamera;
    private final Vector3 tmpVec3;
    private Vector2 direction;



    public GameRenderSystem( final OrthographicCamera camera) {
        this.gameCamera = camera;
        viewport = new FitViewport(12.8f, 7.2f, gameCamera);
        this.spriteBatch = Utils.getSpriteBatch();
        tmpVec3 = new Vector3();
        direction = new Vector2();

        ResourceManager resourceManager = Utils.getResourceManager();
        TiledMap tiledMap = resourceManager.get("map/battle1.tmx", TiledMap.class);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, UNIT_SCALE, spriteBatch);
        layersToRender = tiledMap.getLayers().getByType(TiledMapTileLayer.class);
        viewport.update(1280,720);
    }

    @Override
    public void render(float alpha) {

        ScreenUtils.clear(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        updateCamera(alpha);
        spriteBatch.begin();
        AnimatedTiledMapTile.updateAnimationBaseTime();
        mapRenderer.setView(gameCamera);
        for (TiledMapTileLayer layer : layersToRender) {
            mapRenderer.renderTileLayer(layer);
        }
        spriteBatch.end();
        Gdx.app.debug(TAG, "Game Render System");
    }

    private void updateCamera(float alpha){
        direction.nor().scl(8f * alpha);

        gameCamera.position.x += direction.x;
        gameCamera.position.y += direction.y;

        TiledMapTileLayer layer = layersToRender.get(0);

        float cameraMinX = viewport.getWorldWidth() * 0.5f;
        float cameraMinY = viewport.getWorldHeight() * 0.5f;
        float cameraMaxX = layer.getWidth() * layer.getTileWidth() * UNIT_SCALE - cameraMinX;
        float cameraMaxY = layer.getHeight() * layer.getTileHeight() * UNIT_SCALE -cameraMinY;

        gameCamera.position.x = MathUtils.clamp(gameCamera.position.x, cameraMinX, cameraMaxX);
        gameCamera.position.y = MathUtils.clamp(gameCamera.position.y, cameraMinY, cameraMaxY);
        gameCamera.update();
        direction.set(0,0);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);

        tmpVec3.set(gameCamera.position.x - gameCamera.viewportWidth *0.5f,
                gameCamera.position.y - gameCamera.viewportHeight * 0.5f,
                0);

        gameCamera.project(tmpVec3,
                viewport.getScreenX(), viewport.getScreenY(),
                viewport.getScreenWidth(), viewport.getScreenHeight());
        viewport.setScreenY((int)tmpVec3.y);

        tmpVec3.set(gameCamera.position.x + gameCamera.viewportWidth * 0.5f,
                gameCamera.position.y+ gameCamera.viewportHeight * 0.5f,
                0);
        gameCamera.project(tmpVec3,
                viewport.getScreenX(), viewport.getScreenY(),
                viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    @Override
    public void dispose() {

    }

    @Override
    public void mapChanged(Map map) {
        mapRenderer.setMap(map.getTiledMap());
        layersToRender = map.getTiledMap().getLayers().getByType(TiledMapTileLayer.class);
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {
        switch (key){
            case LEFT -> direction.x = -1f;
            case RIGHT -> direction.x = 1f;
            case UP -> direction.y = 1f;
            case DOWN -> direction.y = -1f;
        }
    }

    @Override
    public void keyUP(InputManager manager, EKey key) {

    }
}
