package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.*;

public class BulletMovementSystem extends IteratingSystem {
    private static final String TAG  = BulletMovementSystem.class.getSimpleName();
    private final ECSEngine ecsEngine;

    public BulletMovementSystem(ECSEngine ecsEngine) {
        super(Family.all(BulletComponent.class).get());
        this.ecsEngine = ecsEngine;
        Gdx.app.debug(TAG, this.getClass().getSimpleName() + " instantiated.");
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        final BulletComponent bulletCmp = ECSEngine.bulletCmpMapper.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);
        final SpeedComponent speedCmp = ECSEngine.speedCmpMapper.get(entity);
        final GameObjectComponent2 goCmp = ECSEngine.gameObj2CmpMapper.get(entity);

        final Vector2 worldCenter = b2dCmp.body.getWorldCenter();
        b2dCmp.body.applyLinearImpulse(
                (speedCmp.velocity.x - b2dCmp.body.getLinearVelocity().x) * b2dCmp.body.getMass(),
                (speedCmp.velocity.y - b2dCmp.body.getLinearVelocity().y) * b2dCmp.body.getMass(),
                worldCenter.x, worldCenter.y, true);

    }
}
