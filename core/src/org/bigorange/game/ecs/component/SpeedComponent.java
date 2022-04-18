package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class SpeedComponent implements Component, Pool.Poolable {
    public final Vector2 velocity = new Vector2(0, 0);

    @Override
    public void reset() {
        velocity.set(0, 0);
    }
}
