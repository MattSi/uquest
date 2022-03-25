package org.bigorange.game.ecs.system;

import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.bigorange.game.core.Utils;
import org.bigorange.game.core.ecs.EntityEngine;
import org.bigorange.game.core.ecs.RenderSystem;
import org.bigorange.game.map.Map;
import org.bigorange.game.map.MapListener;

import static org.bigorange.game.UndergroundQuest.UNIT_SCALE;

public class GameRenderSystem implements RenderSystem, MapListener {
    private static final String TAG = GameRenderSystem.class.getSimpleName();


    private final Viewport viewport;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private Array<TiledMapTileLayer> layersToRender;
    private final SpriteBatch spriteBatch;
    private final OrthographicCamera gameCamera;
    private final Vector3 tmpVec3;

    //private final ImmutableArray<Enti>


    public GameRenderSystem(final EntityEngine entityEngine, final OrthographicCamera camera) {
        this.gameCamera = camera;
        viewport = new FitViewport(12.8f, 7.2f, gameCamera);
        this.spriteBatch = Utils.getSpriteBatch();
        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, spriteBatch);
        tmpVec3 = new Vector3();
    }

    @Override
    public void render(float alpha) {

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
}
