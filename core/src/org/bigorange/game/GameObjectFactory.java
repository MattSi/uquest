package org.bigorange.game;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 所有的游戏对象，都由该类生成。
 * 包括，统一的ID生成器，统一的时间戳生成器等。
 */
public class GameObjectFactory {

    private static GameObjectFactory instance = new GameObjectFactory();
    private static AtomicLong idCounter = new AtomicLong();
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;

    private GameObjectFactory(){
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
    }

    public static GameObjectFactory getInstance(){
        return instance;
    }




    private static long createId(){
        return idCounter.getAndIncrement();
    }
}
