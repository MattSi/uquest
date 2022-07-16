package org.bigorange.game.gameobjs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 所有的游戏对象，都由该类生成。
 * 包括，统一的ID生成器，统一的时间戳生成器等。
 */
public class GameObjectFactory {

    private static GameObjectFactory instance = null;
    private ObjectMap<String, GameObjectConfig> gameObjects;
    private static AtomicLong idCounter = new AtomicLong();
    private World world;
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;

    public static String PLAYER_CONFIG = "scripts/player.json";
    public static String DOOR_KEEPER_CONFIG = "scripts/door_keeper.json";
    private GameObjectFactory(){
        world = new World(new Vector2(0,0), true);
        world.setContinuousPhysics(true);
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        gameObjects = new ObjectMap<>();

        gameObjects.put(GameObjectName.PLAYER.toString(), loadGameObjectConfigByPath(PLAYER_CONFIG));
        gameObjects.put(GameObjectName.DOOR_KEEPER.toString(), loadGameObjectConfigByPath(DOOR_KEEPER_CONFIG));
    }

    public static enum GameObjectName {
        PLAYER,
        DOOR_KEEPER,
        FIRE
    }

    public GameObjectConfig getGameObjectConfig(GameObjectName goName){
        return gameObjects.get(goName.toString());
    }
    public static GameObjectFactory getInstance(){
        if(instance == null){
            instance = new GameObjectFactory();
        }
        return instance;
    }

    private static long createId(){
        return idCounter.getAndIncrement();
    }

    private GameObjectConfig loadGameObjectConfigByPath(String gameObjPath){
        Json json = new Json();
        return json.fromJson(GameObjectConfig.class, Gdx.files.internal(gameObjPath));
    }

    public void setWorldContactListener(ContactListener listener){
        world.setContactListener(listener);
    }

    public World getWorld() {
        return world;
    }
}
