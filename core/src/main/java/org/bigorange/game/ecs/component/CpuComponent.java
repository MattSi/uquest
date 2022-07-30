package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import org.bigorange.game.gameobjs.GameObjectType;

/**
 * 标识为CPU Component，并无其他属性用途
 * 有NPC, ENEMY, MISSILE等.
 */
public class CpuComponent implements Component, Pool.Poolable {

    public GameObjectType gameObjectType = GameObjectType.NOT_DEFINED;

    @Override
    public void reset() {
        gameObjectType = GameObjectType.NOT_DEFINED;
    }
}
