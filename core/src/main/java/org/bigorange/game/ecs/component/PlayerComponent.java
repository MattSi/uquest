package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * 标识为Player，并无其他用途
 */
public class PlayerComponent implements Pool.Poolable, Component {

    // 翻滚（瞬移），作为Player的一个基本技能，将这些参数暂时放在PlayerComponent中，待优化
    public static final float periodTime = 0.6f; // 翻滚的时间间隔
    public long startRollingTime;

    @Override
    public void reset() {
        startRollingTime = 0;
    }

    public boolean rollingTriggerOff(){
        final long now = System.currentTimeMillis();
        float deltaTime = (now - startRollingTime) / 1000f;
        if(deltaTime < periodTime){
            // not enough freeze time
            return false;
        } else {
            startRollingTime = now;
            return true;
        }
    }
}
