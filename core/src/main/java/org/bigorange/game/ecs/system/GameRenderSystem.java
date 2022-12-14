package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.bigorange.game.Utils;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.RenderSystem;
import org.bigorange.game.ecs.component.*;
import org.bigorange.game.map.Map;
import org.bigorange.game.map.MapListener;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static org.bigorange.game.GameConfig.UNIT_SCALE;

/**
 * 游戏渲染系统
 */
public class GameRenderSystem implements RenderSystem, MapListener {
    private static final String TAG = GameRenderSystem.class.getSimpleName();

    private final Viewport viewport;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private Array<TiledMapTileLayer> layersToRender;
    private final SpriteBatch spriteBatch;
    private final OrthographicCamera gameCamera;
    private final Vector3 tmpVec3;
    private final ShapeDrawer shapeDrawer;
    private final World world;

    private final ImmutableArray<Entity> gameObjectsForRender;
    private final ImmutableArray<Entity> charactersForRender;
    private final ImmutableArray<Entity> bulletsForRender;
    private final ImmutableArray<Entity> enemiesForRender;
    private final ImmutableArray<Entity> npcsForRender;

    private final Box2DDebugRenderer b2dRenderer;


    Texture tmpTexture;

    public GameRenderSystem(final EntityEngine entityEngine, final World world, final OrthographicCamera camera) {
        this.gameObjectsForRender = entityEngine.
                getEntitiesFor(Family.all(AnimationSimpleComponent.class,
                                GameObjectComponent.class,
                                MapGeneratedComponent.class,
                                Box2DComponent.class).
                        exclude(RemoveComponent.class, PlayerComponent.class).get());

        this.charactersForRender = entityEngine.
                getEntitiesFor(Family.all(AnimationComponent.class,
                                Box2DComponent.class,
                                PlayerComponent.class,
                                SpeedComponent.class).
                        exclude(RemoveComponent.class).get());

        this.bulletsForRender = entityEngine.
                getEntitiesFor(Family.all(AmmoComponent.class).
                        exclude(RemoveComponent.class).get());

        this.enemiesForRender = entityEngine.getEntitiesFor(Family.all(
                AnimationSimpleComponent.class,
                Animation4DirectionsComponent.class,
                SpeedComponent.class,
                Box2DComponent.class).exclude(RemoveComponent.class).get());

        this.npcsForRender = entityEngine.getEntitiesFor(Family.all(
                CpuComponent.class,
                AnimationComponent.class,
                GameObjectComponent2.class).get());

        this.world = world;
        this.gameCamera = camera;
        viewport = new FitViewport(12.8f, 7.2f, gameCamera);
        this.spriteBatch = Utils.getSpriteBatch();
        tmpVec3 = new Vector3();

        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, spriteBatch);
        b2dRenderer = new Box2DDebugRenderer();

        Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0,0);
        tmpTexture = new Texture(pixmap);
        pixmap.dispose();
        shapeDrawer = new ShapeDrawer(spriteBatch, new TextureRegion(tmpTexture, 0,0,1,1));

        Utils.getMapManager().addMapListener(this);
        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }

    @Override
    public void render(float alpha) {

        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
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

        for (Entity entity : enemiesForRender) {
            renderEnemy(entity, alpha);
        }

        for (Entity entity : npcsForRender) {
            renderNpc(entity, alpha);
        }

        for (final Entity entity : charactersForRender) {
            renderPlayer(entity, alpha);
        }

        for (Entity entity : bulletsForRender) {
            renderBullet(entity, alpha);
        }

        //spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        spriteBatch.end();

        b2dRenderer.render(world, gameCamera.combined);
        //Gdx.app.debug(TAG,"Number Of Bullets Entity: " + bulletsForRender.size());
    }

    private void renderBullet(Entity entity, float alpha) {
        final AnimationSimpleComponent aniCmp = ECSEngine.aniCmpMapper.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);
        final BulletComponent bulletCmp = ECSEngine.bulletCmpMapper.get(entity);
        final GameObjectComponent2 goCmp = ECSEngine.gameObj2CmpMapper.get(entity);

        /**
         * 1. find current animation type
         * 2. load animation key frame.
         */
        final Sprite keyFrame = aniCmp.animation.getKeyFrame(aniCmp.aniTimer, true);

        final Vector2 position = b2dCmp != null ? b2dCmp.body.getPosition() : goCmp.spawnLocation;


        keyFrame.setOriginCenter();
        keyFrame.setBounds(position.x, position.y, aniCmp.width * UNIT_SCALE, aniCmp.height * UNIT_SCALE);
        if(bulletCmp!=null) {
            keyFrame.setRotation(bulletCmp.rotation);
        }
        keyFrame.draw(spriteBatch);

        //Gdx.app.log(TAG, "Width: " + aniCmp.width + " Height: " + aniCmp.height);
        //Gdx.app.log(TAG, "Position: " + position.toString());
    }

    private void renderNpc(Entity entity, float alpha){
        final AnimationComponent aniCmp = ECSEngine.aniCmpMapper2.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);
        final CpuCmpClosedToPlayerComponent cpuClosedToPlayerCmp = ECSEngine.cpuCloseToPlayerComMapper.get(entity);

        /**
         * 1. find current animation type
         * 2. load animation key frame.
         */
        //final Animation<Sprite> currentAnimation = aniCmp.animations.get(aniCmp.aniType);
        final AnimationComponent.AnimationPack<Sprite> currAniPack = aniCmp.animations.get(aniCmp.aniType);
        final Sprite keyFrame = currAniPack.animation.getKeyFrame(aniCmp.aniTimer, true);
        final Vector2 position = b2dCmp.body.getPosition();


//        shapeDrawer.filledEllipse(position.x, position.y - aniCmp.currAnimationWidth/2,
//                aniCmp.currAnimationWidth /4, aniCmp.currAnimationHeight/6,0f,
//                Color.BLACK, Color.GRAY);

        keyFrame.setOriginCenter();
        keyFrame.setBounds(
                MathUtils.lerp(b2dCmp.positionBeforeUpdate.x, position.x, alpha) - (aniCmp.currAnimationWidth* 0.5f),
                MathUtils.lerp(b2dCmp.positionBeforeUpdate.y, position.y, alpha) - (aniCmp.currAnimationHeight* 0.5f),
                aniCmp.currAnimationWidth, aniCmp.currAnimationHeight);

        if(cpuClosedToPlayerCmp != null){
            final Color defaultColor = Color.valueOf(spriteBatch.getColor().toString());
            keyFrame.setColor(Color.RED);
            keyFrame.draw(spriteBatch);
            keyFrame.setColor(defaultColor);
        } else {
            keyFrame.draw(spriteBatch);
        }
    }
    private void renderPlayer(Entity entity, float alpha){
        final AnimationComponent aniCmp = ECSEngine.aniCmpMapper2.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);
        final PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(entity);
        final RollingComponent rollingCmp = ECSEngine.rollingCmpMapper.get(entity);

        /**
         * 1. find current animation type
         * 2. load animation key frame.
         */
        final AnimationComponent.AnimationPack<Sprite> currAniPack = aniCmp.animations.get(aniCmp.aniType);
        final Sprite keyFrame = currAniPack.animation.getKeyFrame(aniCmp.aniTimer, true);
        final Vector2 position = b2dCmp.body.getPosition();


        if(rollingCmp == null) {
            shapeDrawer.filledEllipse(position.x, position.y - aniCmp.currAnimationWidth / 2,
                    aniCmp.currAnimationWidth / 4, aniCmp.currAnimationHeight / 6, 0f,
                    Color.BLACK, Color.GRAY);
        }
        keyFrame.setOriginCenter();
        keyFrame.setBounds(
                MathUtils.lerp(b2dCmp.positionBeforeUpdate.x, position.x, alpha) - (aniCmp.currAnimationWidth* 0.5f),
                MathUtils.lerp(b2dCmp.positionBeforeUpdate.y, position.y, alpha) - (aniCmp.currAnimationHeight* 0.5f),
                aniCmp.currAnimationWidth, aniCmp.currAnimationHeight);

        if(rollingCmp != null){
            final Color defaultColor = Color.valueOf(spriteBatch.getColor().toString());
            keyFrame.setColor(rollingCmp.rollingColor);
            keyFrame.draw(spriteBatch);
            keyFrame.setColor(defaultColor);
        } else {
            keyFrame.draw(spriteBatch);
        }

    }

    private void renderEntity(Entity entity, float alpha) {
        final AnimationSimpleComponent aniCmp = ECSEngine.aniCmpMapper.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);
        final PlayerComponent playerCmp = ECSEngine.playerCmpMapper.get(entity);

        if (aniCmp.animation == null) {
            return;
        }
        final Vector2 position = b2dCmp.body.getPosition();
        if(playerCmp != null){
            shapeDrawer.filledEllipse(position.x, position.y - aniCmp.width/2, aniCmp.width /4, aniCmp.height/6
                    ,0f, Color.BLACK, Color.GRAY);
        }


        final Sprite keyFrame = aniCmp.animation.getKeyFrame(aniCmp.aniTimer, true);
        //keyFrame.setColor(Color.WHITE);
        keyFrame.setOriginCenter();
        keyFrame.setBounds(
                MathUtils.lerp(b2dCmp.positionBeforeUpdate.x, position.x, alpha) - (aniCmp.width * 0.5f),
                MathUtils.lerp(b2dCmp.positionBeforeUpdate.y, position.y, alpha) - (aniCmp.height * 0.5f),
                aniCmp.width, aniCmp.height);

        keyFrame.draw(spriteBatch);

    }

    private void renderEnemy(Entity entity, float alpha) {
        final AnimationSimpleComponent aniCmp = ECSEngine.aniCmpMapper.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);
        final GameObjectComponent gameObjectCmp = ECSEngine.gameObjCmpMapper.get(entity);

        if (aniCmp.animation == null) {
            return;
        }
        final Vector2 position = b2dCmp.body.getPosition();

        final Sprite keyFrame = aniCmp.animation.getKeyFrame(aniCmp.aniTimer, true);
        //keyFrame.setColor(Color.WHITE);
        keyFrame.setOriginCenter();
        keyFrame.setBounds(
                position.x - (aniCmp.width * 0.5f),
                position.y - (aniCmp.height * 0.5f),
                aniCmp.width, aniCmp.height);

        if (gameObjectCmp.findPlayer) {
            final Color defaultColor = Color.valueOf(spriteBatch.getColor().toString());
            keyFrame.setColor(Color.RED);
            keyFrame.draw(spriteBatch);
            keyFrame.setColor(defaultColor);
        } else {
            keyFrame.draw(spriteBatch);
        }
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
        if(tmpTexture != null){
            tmpTexture.dispose();
        }
    }

    @Override
    public void mapChanged(Map map) {
        mapRenderer.setMap(map.getTiledMap());
        layersToRender = map.getTiledMap().getLayers().getByType(TiledMapTileLayer.class);
    }

}
