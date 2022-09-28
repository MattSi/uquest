package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import org.bigorange.game.ActionType;

/**
 * 定义一个游戏对象的行为类型
 * 行为可以是： 交谈TALK， 箱子CHEST， 等等
 */
public class ActionableComponent implements Component, Pool.Poolable {
    public ActionType type = ActionType.UNDEFINED;
    public String talkScript;

    @Override
    public void reset() {
        type = ActionType.UNDEFINED;
        talkScript = null;
    }
}

