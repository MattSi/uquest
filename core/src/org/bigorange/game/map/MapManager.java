package org.bigorange.game.map;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.core.Utils;

/**
 * 1. Load a map from resource manager
 * 2. Register map receivers
 * 3. Notify map receivers when a map is changed.
 * 4. Spawn collision areas (ECS required)
 * 5. Spawn game objects (ECS required)
 * 6. Generate and cache animated game objects
 *
 */
public class MapManager {
    public static final String TAG = MapManager.class.getName();
    private Map currentMap;

    private final IntMap<Animation<Sprite>> gameObjectAnimationCache;
    private final Array<MapListener> mapListeners;
    private final ResourceManager resourceManager;

    public MapManager(){
        this.currentMap = null;
        this.gameObjectAnimationCache = new IntMap<>();
        this.mapListeners = new Array<>();
        this.resourceManager = Utils.getResourceManager();
    }

    public MapManager(ResourceManager resourceManager){
        this.currentMap = null;
        this.gameObjectAnimationCache = new IntMap<>();
        this.mapListeners = new Array<>();
        this.resourceManager = resourceManager;
    }

    public void loadMap(TiledMap map){
        if(currentMap == null){
            currentMap = new Map(map);
        }
        for (final MapListener mapListener : mapListeners) {
            mapListener.mapChanged(currentMap);
        }

        for (final GameObject gameObj : currentMap.getGameObjects()) {
            getAnimation(gameObj);
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
}
