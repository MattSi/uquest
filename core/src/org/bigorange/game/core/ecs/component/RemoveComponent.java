package org.bigorange.game.core.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class RemoveComponent implements Component, Pool.Poolable {

    @Override
    public void reset() {
        // This component is only used to remove entities the end of an update loop
    }
}
