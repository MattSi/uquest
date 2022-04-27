package org.bigorange.game.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class GameObjectComponent implements Component, Pool.Poolable {

    public GameObjectType type;
    public int id;

    @Override
    public void reset() {
        type = null;
        id = 0;
    }

    public enum GameObjectType {
        NOT_DEFINED, TREE, WALL, MISSILE, ENEMY, NPC
    }
}
