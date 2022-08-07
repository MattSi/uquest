package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * 武器弹药组件, 包括子弹,导弹,爆炸效果等
 * 用于属性控制和渲染识别
 */
public class AmmoComponent implements Component, Pool.Poolable {
    @Override
    public void reset() {

    }
}
