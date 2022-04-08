package org.bigorange.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
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
    private final Array<MapGameObject> gameObjects;
    private final Array<CollisionArea> collisionAreas;
    private final Array<Vector2> playerStartLocations;
    private final Array<Vector2> npcStartLocations;


    public Map(TiledMap tiledMap){
        final MapProperties mapProps = tiledMap.getProperties();
        this.tiledMap = tiledMap;
        this.gameObjects = new Array<>();
        this.collisionAreas = new Array<>();

        this.playerStartLocations = new Array<>();
        this.npcStartLocations = new Array<>();

        parseGameObjects();
        parseCollision();
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
                MapGameObject mapGameObject = new MapGameObject(tiledMapObject);
                gameObjects.add(mapGameObject);
            } else {
                Gdx.app.log(TAG, "Unsupported mapObject for objects layer: " + item);
            }
        }
    }

    private void parseCollision() {
        final MapLayer collisionLayer = tiledMap.getLayers().get("Collision");
        if (collisionLayer == null) {
            Gdx.app.log(TAG, "Map does not has a layer called 'Collision'.");
            return;
        }

        for (MapObject mapObj : collisionLayer.getObjects()) {
            if (mapObj instanceof PolylineMapObject polylineMapObject) {
                final Polyline polyline = polylineMapObject.getPolyline();
                collisionAreas.add(new CollisionArea(polyline.getX(), polyline.getY(), polyline.getVertices()));
            } else if (mapObj instanceof PolygonMapObject polygonMapObject){
                final Polygon polygon = polygonMapObject.getPolygon();
                collisionAreas.add(new CollisionArea(polygon.getX(), polygon.getY(), polygon.getVertices()));
            }else if (mapObj instanceof RectangleMapObject rectangleMapObject) {
                final Rectangle rectangle = rectangleMapObject.getRectangle();
                final float[] rectVertices = new float[10];

                // left-bot
                rectVertices[0] = 0;
                rectVertices[1] = 0;

                // left-top
                rectVertices[2] = 0;
                rectVertices[3] = rectangle.height;

                // right-top
                rectVertices[4] = rectangle.width;
                rectVertices[5] = rectangle.height;

                // right-bot
                rectVertices[6] = rectangle.width;
                rectVertices[7] = 0;

                // left-bot
                rectVertices[8] = 0;
                rectVertices[9] = 0;
                collisionAreas.add(new CollisionArea(rectangle.x, rectangle.y, rectVertices));
            } else {
                Gdx.app.log(TAG, "Unsupported mapObject for collision layer." + mapObj);
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
                final MapProperties props = rectangleMapObject.getProperties();
                if(props.containsKey(spawnType)) {
                    type = SpawnType.valueOf(props.get(spawnType, String.class).toUpperCase());
                } else {
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

    public Array<MapGameObject> getGameObjects() {
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

    public Array<CollisionArea> getCollisionAreas() {
        return collisionAreas;
    }
}
