package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import org.bigorange.game.gameobjs.Direction;
import org.bigorange.game.gameobjs.State;

/**
 * 所有的GameObject都需要此Component
 */
public class GameObjectComponent2 implements Component, Pool.Poolable {
    public String gameObjId;
    public State state = State.IDLE;
    public long birthTime;
    public boolean isMapGenerated;
    public Vector2 spawnLocation = new Vector2(Vector2.Zero);
    public Direction direction = Direction.DOWN;

    @Override
    public void reset() {
        birthTime = 0L;
        gameObjId = null;
        state = State.IDLE;
        isMapGenerated = false;
        spawnLocation.setZero();
        direction = Direction.DOWN;
    }
}
