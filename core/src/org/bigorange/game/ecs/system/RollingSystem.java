package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.component.AnimationComponent2;
import org.bigorange.game.ecs.component.InvincibilityComponent;
import org.bigorange.game.ecs.component.RollingComponent;
import org.bigorange.game.ecs.component.SpeedComponent;

public class RollingSystem extends IteratingSystem {
    private static final String TAG = RollingSystem.class.getSimpleName();

    public RollingSystem() {
        super(Family.all(RollingComponent.class).get());
        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        /**
         * 1. 改变Entity颜色
         * 2. 冻结Entity的动画
         * 3. 无敌状态 (前半程)
         * 4. 速度1.5倍
         * 5. 阻止键盘操作 (过程中)
         */
        final RollingComponent rollingCmp = EntityEngine.rollingCmpMapper.get(entity);
        final SpeedComponent speedCmp = EntityEngine.speedCmpMapper.get(entity);
        final AnimationComponent2 aniCmp = EntityEngine.aniCmpMapper2.get(entity);
        final float times = 6f;

        if(rollingCmp == null){
            return;
        }

        // 结束
        if (rollingCmp.deltaTime > RollingComponent.rollingTime) {
            rollingCmp.isRolling = false;
            rollingCmp.isTriggered = false;

            speedCmp.velocity.y /= times;
            speedCmp.velocity.x /= times;

            entity.remove(InvincibilityComponent.class);
            entity.remove(RollingComponent.class);

            final PlayerControlSystem playerControlSystem = getEngine().getSystem(PlayerControlSystem.class);
            playerControlSystem.setEnabled(true);
            playerControlSystem.refreshKeys();
            aniCmp.isEnable = true;
            return;
        }

        // 开始
        if (!rollingCmp.isTriggered) {
            rollingCmp.isTriggered = true;
            rollingCmp.isRolling = true;

            speedCmp.velocity.y *= times;
            speedCmp.velocity.x *= times;

            final InvincibilityComponent component = getEngine().createComponent(InvincibilityComponent.class);
            entity.add(component);

            getEngine().getSystem(PlayerControlSystem.class).setEnabled(false);
            aniCmp.isEnable = false;
            return;
        }


        // Rolling过程中
        if(rollingCmp.isRolling ) {
            final int length = RollingComponent.RollingColor.values().length;
            final int random = MathUtils.random(length - 1);
            final RollingComponent.RollingColor rdmColor = RollingComponent.RollingColor.values()[random];
            rollingCmp.rollingColor = rdmColor.getColor();
            rollingCmp.deltaTime += deltaTime;
            Gdx.app.log(TAG, "DeltaTime: " + rollingCmp.deltaTime);
        }
    }
}
