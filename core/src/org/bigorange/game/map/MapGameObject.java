package org.bigorange.game.map;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import org.bigorange.game.ecs.component.GameObjectComponent;

import static org.bigorange.game.UndergroundQuest.UNIT_SCALE;

/**
 * 游戏对象定义，这些游戏对象由地图生成，包含以下元素
 * 1) Tiled ID
 * 2) 实际地图对象的引用
 * 3）对象的边界，这些边界由地图对象组成
 * 4）动画时长，由地图编辑器生成
 * 5）游戏对象类型，在地图编辑器中添加特定的属性生成
 *
 *
 * ===============================
 * 该类只有属性，没有方法。目的是获取Tiled Map中Objects层的游戏对象
 */
public class MapGameObject {
    private final int id;
    private final TiledMapTileMapObject tileMapObjectRef;
    private final Rectangle boundaries;
    private final float animationInterval;
    private final GameObjectComponent.GameObjectType type;

    public MapGameObject(final TiledMapTileMapObject tileMapObject) {
        final MapProperties props = tileMapObject.getProperties();
        final MapProperties tileProps = tileMapObject.getTile().getProperties();

        this.id = props.get("id", Integer.class);
        this.tileMapObjectRef = tileMapObject;
        this.boundaries = new Rectangle();
        this.boundaries.setPosition(props.get("x", Float.class) * UNIT_SCALE, props.get("y", Float.class) * UNIT_SCALE);
        this.boundaries.setSize(props.get("width", Float.class) * UNIT_SCALE, props.get("height", Float.class) * UNIT_SCALE );

        if(tileMapObject.getTile() instanceof AnimatedTiledMapTile animatedTiledMapTile){
            animationInterval = animatedTiledMapTile.getAnimationIntervals()[0] * 0.001f;
        } else {
            animationInterval = 0f;
        }

        if(props.containsKey("type")){
            type = GameObjectComponent.GameObjectType.valueOf(props.get("type", String.class));
        } else if(tileProps.containsKey("type")){
            type = GameObjectComponent.GameObjectType.valueOf(tileProps.get("type", String.class));
        } else {
            type = GameObjectComponent.GameObjectType.NOT_DEFINED;
        }
    }

    public int getId() {
        return id;
    }

    public TiledMapTile getTile() {
        return tileMapObjectRef.getTile();
    }

    public Rectangle getBoundaries() {
        return boundaries;
    }

    public float getAnimationInterval() {
        return animationInterval;
    }

    public GameObjectComponent.GameObjectType getType() {
        return type;
    }
}
