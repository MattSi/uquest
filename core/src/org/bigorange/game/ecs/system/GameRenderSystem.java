package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.*;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.input.KeyInputListener;
import org.bigorange.game.utils.Utils;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.RenderSystem;
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

    private final ImmutableArray<Entity> gameObjectsForRender;
    private final ImmutableArray<Entity> charactersForRender;



    public GameRenderSystem(final EntityEngine entityEngine, final OrthographicCamera camera) {
        this.gameObjectsForRender = entityEngine.
                getEntitiesFor(Family.all(AnimationComponent.class, GameObjectComponent.class).
                        exclude(RemoveComponent.class).get());

        this.charactersForRender = entityEngine.
                getEntitiesFor(Family.all(AnimationComponent.class, Box2DComponent.class, PlayerComponent.class).
                        exclude(RemoveComponent.class).get());

        this.gameCamera = camera;
        viewport = new FitViewport(12.8f, 7.2f, gameCamera);
        this.spriteBatch = Utils.getSpriteBatch();
        tmpVec3 = new Vector3();

        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, spriteBatch);

        Utils.getMapManager().addMapListener(this);
        Gdx.app.log(TAG, "instantiated.");
    }

    @Override
    public void render(float alpha) {

        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        updateCamera(alpha);
        spriteBatch.begin();
        AnimatedTiledMapTile.updateAnimationBaseTime();
        mapRenderer.setView(gameCamera);
        for (TiledMapTileLayer layer : layersToRender) {
            mapRenderer.renderTileLayer(layer);
        }

        for (final Entity entity : gameObjectsForRender) {
            renderEntity(entity, alpha);
        }

        for (final Entity entity : charactersForRender) {
            renderEntity(entity, alpha);
        }

        spriteBatch.end();
    }

    private void renderEntity(Entity entity, float alpha) {
        final AnimationComponent aniCmp = ECSEngine.aniCmpMapper.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);

        if (aniCmp.animation == null) {
            return;
        }

        final Vector2 position = b2dCmp.body.getPosition();
        final Sprite keyFrame = aniCmp.animation.getKeyFrame(aniCmp.aniTimer, true);
        keyFrame.setColor(Color.WHITE);
        keyFrame.setOriginCenter();
        keyFrame.setBounds(
                MathUtils.lerp(b2dCmp.positionBeforeUpdate.x, position.x, alpha) - (aniCmp.width * 0.5f),
                MathUtils.lerp(b2dCmp.positionBeforeUpdate.y, position.y, alpha) - (aniCmp.height * 0.5f),
                aniCmp.width, aniCmp.height);

        keyFrame.draw(spriteBatch);
    }

    private void updateCamera(float alpha){

        TiledMapTileLayer layer = layersToRender.get(0);

        float cameraMinX = viewport.getWorldWidth() * 0.5f;
        float cameraMinY = viewport.getWorldHeight() * 0.5f;
        float cameraMaxX = layer.getWidth() * layer.getTileWidth() * UNIT_SCALE - cameraMinX;
        float cameraMaxY = layer.getHeight() * layer.getTileHeight() * UNIT_SCALE -cameraMinY;

        gameCamera.position.x = MathUtils.clamp(gameCamera.position.x, cameraMinX, cameraMaxX);
        gameCamera.position.y = MathUtils.clamp(gameCamera.position.y, cameraMinY, cameraMaxY);
        gameCamera.update();
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
