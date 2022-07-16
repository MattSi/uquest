package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * 标识为NPC，并无其他属性用途
 */
public class NpcComponent implements Component, Pool.Poolable {
    @Override
    public void reset() {

    }
}
