package org.bigorange.game.map;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import org.bigorange.game.ecs.component.GameObjectType;

import static org.bigorange.game.UndergroundQuest.UNIT_SCALE;

public class GameObject {
    private final int id;
    private final TiledMapTileMapObject tileMapObjectRef;
    private final Rectangle boundaries;
    private final float animationInterval;
    private final GameObjectType type;

    GameObject(final TiledMapTileMapObject tileMapObject) {
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
            type = GameObjectType.valueOf(props.get("type", String.class));
        } else if(tileProps.containsKey("type")){
            type = GameObjectType.valueOf(tileProps.get("type", String.class));
        } else {
            type = GameObjectType.NOT_DEFINED;
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

    public GameObjectType getType() {
        return type;
    }
}
