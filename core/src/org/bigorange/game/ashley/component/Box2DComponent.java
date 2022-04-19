package org.bigorange.game.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class Box2DComponent implements Component, Pool.Poolable {
    public Body body;
    public final Vector2 positionBeforeUpdate = new Vector2();
    public float width;
    public float height;
    public float x, y;


    @Override
    public void reset() {
        width = height = 0f;
        x = y = 0f;
        positionBeforeUpdate.set(0, 0);

        if (body != null) {
            body.getWorld().destroyBody(body);
            body = null;
        }
    }
}
