package org.bigorange.game.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class EnemyComponent implements Component, Pool.Poolable {
    public EnemyState state = EnemyState.IDLE;
    public float maxSpeed;
    public boolean findPlayer;

    @Override
    public void reset() {
        maxSpeed = 0f;
        state = EnemyState.IDLE;
        findPlayer = false;
    }

    public enum EnemyState{
        IDLE,
        PATROL,
        ATTACK,
        RUN_AWAY
    }
}
