package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class NpcComponent implements Component, Pool.Poolable {
    public NpcStatus status;
    public Vector2 spawn = new Vector2(0,0);
    public int id;

    @Override
    public void reset() {
        status = null;
        spawn.set(0,0);
        id = 0;
    }

    public enum NpcStatus{
        IDLE, PATROL
    }
}
