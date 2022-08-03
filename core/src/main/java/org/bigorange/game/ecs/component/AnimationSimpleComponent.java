package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;

/**
 *  简单动画组件, 状态较少,适合子弹,烟雾等对象
 */
public class AnimationSimpleComponent implements Component, Pool.Poolable {
    public Animation<Sprite> animation;
    public float width;
    public float height;
    public float aniTimer;

    @Override
    public void reset() {
        animation = null;
        width = 0f;
        height = 0f;
        aniTimer = 0f;
    }
}
