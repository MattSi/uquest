package org.bigorange.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.ecs.component.SpawnType;

/**
 * Parse map, get gameObjects, collide area, etc
 *
 * 地图类，生成地图信息， 包括
 * 1） 游戏对象（地图）
 * 2） 碰撞区域
 * 3） 起始点，怪物再生点
 *
 * ============================
 *  不参与MapRenderer的渲染工作
 *
 */
public class Map {
    public static final String TAG = Map.class.getSimpleName();

    private final TiledMap tiledMap;
    private final Array<GameObject> gameObjects;
    private final Array<Vector2> playerStartLocations;
    private final Array<Vector2> npcStartLocations;


    public Map(TiledMap tiledMap){
        final MapProperties mapProps = tiledMap.getProperties();
        this.tiledMap = tiledMap;
        this.gameObjects = new Array<>();
        float startX = mapProps.get("playerStartX", 0f, Float.class);
        float startY = mapProps.get("playerStartY", 0f, Float.class);

        this.playerStartLocations = new Array<>();
        this.npcStartLocations = new Array<>();

        parseGameObjects();
        parseSpawnLocations();

    }

    /**
     * parse all objects from object layer of a tiled map
     */
    private void parseGameObjects(){
        final MapLayer objLayer = tiledMap.getLayers().get("Objects");
        if(objLayer == null){
            Gdx.app.error(TAG, "Map does not has a layer called 'Objects'.");
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

    private void parseSpawnLocations(){
        final MapLayer spawnLayer = tiledMap.getLayers().get("Spawn");
        if(spawnLayer == null){
            Gdx.app.error(TAG, "Map does not has a layer called 'Spawn'.");
            return;
        }

        for (MapObject item : spawnLayer.getObjects()) {
            if(item instanceof  RectangleMapObject rectangleMapObject){
                SpawnType type;
                String spawnType = "spawnType";
               // item.get
                final MapProperties props = rectangleMapObject.getProperties();
                //final MapProperties tileProps = rectangleMapObject.
                if(props.containsKey(spawnType)) {
                    type = SpawnType.valueOf(props.get(spawnType, String.class).toUpperCase());
                }
//                } else if( tileProps.containsKey(spawnType)){
//                    type = SpawnType.valueOf(tileProps.get(spawnType, String.class));
//                } else {
                else {
                    type = SpawnType.NPC;
                }

                if(type == SpawnType.PLAYER){
                    playerStartLocations.add(new Vector2(rectangleMapObject.getRectangle().x, rectangleMapObject.getRectangle().y));
                } else {
                    npcStartLocations.add(new Vector2(new Vector2(rectangleMapObject.getRectangle().x, rectangleMapObject.getRectangle().y)));
                }
            }
        }

    }

    public Array<GameObject> getGameObjects() {
        return gameObjects;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Array<Vector2> getPlayerStartLocations() {
        return playerStartLocations;
    }

    public Array<Vector2> getNpcStartLocations() {
        return npcStartLocations;
    }
}
