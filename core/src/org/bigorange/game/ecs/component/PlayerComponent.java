package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * 标识为Player，并无其他用途
 */
public class PlayerComponent implements Pool.Poolable, Component {
    @Override
    public void reset() {
    }
}
