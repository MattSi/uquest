package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import org.bigorange.game.Utils;
import org.bigorange.game.WorldContactManager;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.EntityEngine;
import org.bigorange.game.ecs.component.Box2DComponent;
import org.bigorange.game.ecs.component.RemoveComponent;

public class BulletPlayerContactSystem extends EntitySystem
        implements WorldContactManager.WorldBulletContactListener {
    public static final String TAG = BulletPlayerContactSystem.class.getSimpleName();
    private final ECSEngine ecsEngine;
    public BulletPlayerContactSystem(ECSEngine ecsEngine){
        this.ecsEngine = ecsEngine;
        Utils.getWorldContactManager().addWorldBulletContactListener(this);
        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }


    @Override
    public void beginContact(Entity bullet, Entity bulletContact) {

        //Gdx.app.log(TAG, "111111111111111111111111111111111111111111111");
        /**
         * 1. 子弹消失
         * 2. 产生爆炸效果
         * 3. 被撞击物生命值减少
         */
        final Box2DComponent b2dCmp = EntityEngine.b2dCmpMapper.get(bullet);
        ecsEngine.addExplosion1(new Vector2(b2dCmp.body.getPosition()), null);
        bullet.add(ecsEngine.createComponent(RemoveComponent.class));
    }

    @Override
    public void endContact(Entity bullet, Entity bulletContact) {
    }
}
