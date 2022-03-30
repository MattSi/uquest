package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class Box2DComponent implements Component, Pool.Poolable {
    public float width;
    public float height;
    public float x;
    public float y;

    @Override
    public void reset() {
        x = y = 0f;
        width = height = 0f;
    }
}
