package org.bigorange.game.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BulletComponent implements Component, Pool.Poolable {
    public float maxSpeed;
    public long startTime;

    @Override
    public void reset() {
        maxSpeed = 0;
        startTime = 0;
    }
}
