package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ManaComponent implements Component, Pool.Poolable {
    public float currentMana;
    public float maxMana;

    @Override
    public void reset() {
        currentMana = 0f;
        maxMana = 0f;
    }
}
