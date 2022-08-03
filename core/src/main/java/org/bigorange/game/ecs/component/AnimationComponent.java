package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;
import org.bigorange.game.gameobjs.AnimationType;

import java.util.EnumMap;

/**
 * 动画组件，同之前的区别在于，将动画的类型也包含在内，减轻System的负担
 */
public class AnimationComponent implements Component, Pool.Poolable {
    public EnumMap<AnimationType, AnimationPack<Sprite>> animations;
    public AnimationType aniType;
    public float aniTimer;
    public int stopFrameIndex;
    public float currAnimationWidth;
    public float currAnimationHeight;
    public boolean isEnable;

    @Override
    public void reset() {
        animations.clear();
        animations = null;
        aniType = null;
        aniTimer = 0f;
        stopFrameIndex = 0;
        currAnimationHeight = 0f;
        currAnimationWidth = 0f;
        isEnable = true;
    }

    public static class AnimationPack<T> {
        public int width;
        public int height;
        public Animation<T> animation;

        public AnimationPack() {
        }

    }
}
