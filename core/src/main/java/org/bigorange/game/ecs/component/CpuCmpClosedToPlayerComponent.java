package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * CPU组件,靠近玩家Component
 */
public class CpuCmpClosedToPlayerComponent implements Component, Pool.Poolable {
    @Override
    public void reset() {
    }
}
