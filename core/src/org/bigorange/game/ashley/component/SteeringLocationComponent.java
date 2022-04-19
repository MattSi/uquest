package org.bigorange.game.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import org.bigorange.game.ai.steering.Box2DLocationAdapter;

public class SteeringLocationComponent extends Box2DLocationAdapter implements Component, Pool.Poolable {


    @Override
    public void reset() {
        this.body = null;
    }
}
