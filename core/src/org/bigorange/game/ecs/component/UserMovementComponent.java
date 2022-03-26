package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class UserMovementComponent implements Component, Pool.Poolable {
    public Vector2 position = new Vector2();


    @Override
    public void reset() {
        position.set(0, 0);
    }
}
