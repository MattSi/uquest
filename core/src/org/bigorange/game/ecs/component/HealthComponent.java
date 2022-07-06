package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class HealthComponent implements Component, Pool.Poolable {
    public float currentHealth;
    public float maxHealth;

    @Override
    public void reset() {
        currentHealth = 0f;
        maxHealth = 0f;
    }
}
