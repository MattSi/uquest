package org.bigorange.game.map;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import org.bigorange.game.ResourceManager;
import org.bigorange.game.Utils;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.RemoveComponent;
import org.bigorange.game.message.MessageType;

import static org.bigorange.game.GameConfig.CATEGORY_PLAYER;
import static org.bigorange.game.GameConfig.CATEGORY_WORLD;


/**
 * 1. Load a map from resource manager
 * 2. Register map receivers
 * 3. Notify map receivers when a map is changed.
 * 4. Spawn collision areas (ECS required)
 * 5. Spawn game objects (ECS required)
 * 6. Generate and cache animated game objects
 *
 */
public class MapManager implements Telegraph {
    public static final String TAG = MapManager.class.getName();
    private Map currentMap;

    private final IntMap<Animation<Sprite>> gameObjectAnimationCache;
    private final Array<MapListener> mapListeners;
    private final ResourceManager resourceManager;
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;

    public MapManager(){
        this.currentMap = null;
        this.gameObjectAnimationCache = new IntMap<>();
        this.mapListeners = new Array<>();
        this.resourceManager = Utils.getResourceManager();
        this.bodyDef = new BodyDef();
        this.fixtureDef = new FixtureDef();
        MessageManager.getInstance().addListener(this, MessageType.MSG_PLAYER_CHANGE_MAP);
    }

    public MapManager(ResourceManager resourceManager){
        this.currentMap = null;
        this.gameObjectAnimationCache = new IntMap<>();
        this.mapListeners = new Array<>();
        this.resourceManager = resourceManager;
        this.bodyDef = new BodyDef();
        this.fixtureDef = new FixtureDef();
    }

    public void loadMap(TiledMap map, final World world){
        if(currentMap == null){
            currentMap = new Map(map);
        }
        for (final MapListener mapListener : mapListeners) {
            mapListener.mapChanged(currentMap);
        }

        for (final GameObject gameObj : currentMap.getGameObjects()) {
            getAnimation(gameObj);
        }
        spawnCollisionAreas(world);
    }


    private void spawnCollisionAreas(final World world){
        if(currentMap == null){
            Gdx.app.error(TAG, "Can not spawn collision areas of null map.");
            return;
        }

        for (final CollisionArea collisionArea : currentMap.getCollisionAreas()) {
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(collisionArea.getStartLocation());

            final Body body = world.createBody(bodyDef);
            final ChainShape shape = new ChainShape();
            shape.createChain(collisionArea.getVertices());

            fixtureDef.shape = shape;
            fixtureDef.friction = 0;
            fixtureDef.isSensor=false;
            fixtureDef.filter.categoryBits = CATEGORY_WORLD;
            fixtureDef.filter.maskBits = CATEGORY_PLAYER;
            body.createFixture(fixtureDef);
            shape.dispose();
        }

    }


    public void spawnGameObjects(final ECSEngine ecsEngine, final ImmutableArray<Entity> gameObjects){
        /**
         *  1. Clear existing gameobjects.
         *  2. Spawn gameobjects from the current loaded map.
         */
        for (Entity gameObj : gameObjects) {
            gameObj.add(ecsEngine.createComponent(RemoveComponent.class));
        }

        if(currentMap == null){
            Gdx.app.error(TAG, "Cannot spawn game objects from a null map.");
            return;
        }

        // 只适合单一动画场景，对于NPC这种复杂的GameObject不适用了
        for (GameObject gameObj : currentMap.getGameObjects()) {
            ecsEngine.addMapGameObject(gameObj, getAnimation(gameObj));
        }

    }

    private Animation<Sprite> getAnimation(final GameObject gameObj) {
        final TiledMapTile tile = gameObj.getTile();
        Animation<Sprite> animation = gameObjectAnimationCache.get(tile.getId());
        if (animation != null) {
            return animation;
        }
        if (tile instanceof AnimatedTiledMapTile aniTile) {
            final Array<Sprite> keyFrames = new Array<>();
            for (final StaticTiledMapTile staticTile : aniTile.getFrameTiles()) {
                keyFrames.add(new Sprite(staticTile.getTextureRegion()));
            }
            animation = new Animation<>(gameObj.getAnimationInterval(), keyFrames, Animation.PlayMode.LOOP);
            gameObjectAnimationCache.put(tile.getId(), animation);
        } else if(tile instanceof  StaticTiledMapTile ){
            animation = new Animation<Sprite>(0, new Sprite(tile.getTextureRegion()));
            gameObjectAnimationCache.put(tile.getId(), animation);
        } else {
            Gdx.app.error(TAG, "Unsupported TiledMapTile type: " + tile);
        }

        return animation;
    }

    public void addMapListener(MapListener mapListener){
        if(!mapListeners.contains(mapListener, true)){
            mapListeners.add(mapListener);
        }
    }

    public void removeMapListener(MapListener mapListener){
        if(mapListeners.contains(mapListener, true)){
            mapListeners.removeValue(mapListener, true);
        }
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public IntMap<Animation<Sprite>> getGameObjectAnimationCache() {
        return gameObjectAnimationCache;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        Gdx.app.debug(TAG, ""+ (String) msg.extraInfo);
//        final TiledMap tiledMap = resourceManager.get("map/battle2.tmx", TiledMap.class);
//        mapManager.loadMap(tiledMap, world);
//        mapManager.spawnGameObjects(this.ecsEngine, this.ecsEngine.getGameObjEntities());
        return true;
    }
}
