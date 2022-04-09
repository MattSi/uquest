package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class BulletComponent implements Component, Pool.Poolable {
    public final Vector2 speed =new Vector2();
    public float maxSpeed;
    public long startTime;

    @Override
    public void reset() {
        speed.set(0,0);
        maxSpeed = 0;
        startTime = 0;
    }
}
