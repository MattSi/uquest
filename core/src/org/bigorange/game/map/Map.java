package org.bigorange.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Parse map, get gameObjects, collide area, etc
 */
public class Map {
    public static final String TAG = Map.class.getSimpleName();

    private final TiledMap tiledMap;
    private final Array<GameObject> gameObjects;
    private final Vector2 startLocation;


    public Map(TiledMap tiledMap){
        final MapProperties mapProps = tiledMap.getProperties();
        this.tiledMap = tiledMap;
        this.gameObjects = new Array<>();
        float startX = mapProps.get("playerStartX", 0f, Float.class);
        float startY = mapProps.get("playerStartY", 0f, Float.class);
        this.startLocation = new Vector2(startX, startY);

        parseGameObjects();

    }

    /**
     * parse all objects from object layer of a tiled map
     */
    private void parseGameObjects(){
        final MapLayer objLayer = tiledMap.getLayers().get("Objects");
        if(objLayer == null){
            Gdx.app.error(TAG, "Map does not has a layer call 'objects'.");
            return;
        }

        for (MapObject item : objLayer.getObjects()) {
            if(item instanceof TiledMapTileMapObject tiledMapObject){
                GameObject gameObject = new GameObject(tiledMapObject);
                gameObjects.add(gameObject);
            } else {
                Gdx.app.log(TAG, "Unsupported mapObject for objects layer: " + item);
            }
        }
    }

    public Array<GameObject> getGameObjects() {
        return gameObjects;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }
}
