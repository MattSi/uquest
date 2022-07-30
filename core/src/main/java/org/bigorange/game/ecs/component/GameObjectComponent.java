package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import org.bigorange.game.gameobjs.GameObjectState;
import org.bigorange.game.gameobjs.GameObjectType;

public class GameObjectComponent implements Component, Pool.Poolable {

    public int id;
    public GameObjectType type;
    public GameObjectState state;
    public boolean isMapGenerated;
    public boolean findPlayer;
    public long birthTime;
    public Vector2 spawnLocation = new Vector2();

    @Override
    public void reset() {
        type = null;
        state = null;
        id = 0;
        isMapGenerated = false;
        findPlayer = false;
        birthTime = 0L;
        spawnLocation.setZero();
    }

}
