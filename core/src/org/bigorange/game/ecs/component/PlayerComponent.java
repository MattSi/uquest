package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class PlayerComponent implements Pool.Poolable, Component {
    public float maxSpeed;

    public float maxHealth;

    @Override
    public void reset() {
        maxSpeed = 0;
        maxHealth = 0;
    }
}
