package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;


public class BulletComponent implements Component, Pool.Poolable {
    public Vector2 target;
    public float rotation;

    @Override
    public void reset() {
        target = null;
        rotation = 0f;
    }
}
