package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DeathClockComponent implements Component, Pool.Poolable {
    public float tick;
    @Override
    public void reset() {
        tick = 0f;
    }
}
