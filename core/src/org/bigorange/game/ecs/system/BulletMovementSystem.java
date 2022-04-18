package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.Box2DComponent;
import org.bigorange.game.ecs.component.BulletComponent;
import org.bigorange.game.ecs.component.RemoveComponent;
import org.bigorange.game.ecs.component.SpeedComponent;

public class BulletMovementSystem extends IteratingSystem {
    private final ECSEngine ecsEngine;

    public BulletMovementSystem(ECSEngine ecsEngine) {
        super(Family.all(BulletComponent.class, Box2DComponent.class).get());
        this.ecsEngine = ecsEngine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        final BulletComponent bulletCmp = ECSEngine.bulletCmpMapper.get(entity);
        final Box2DComponent b2dCmp = ECSEngine.b2dCmpMapper.get(entity);
        final SpeedComponent speedCmp = ECSEngine.speedCmpMapper.get(entity);
        final long currentTimeMillis = System.currentTimeMillis();

        final Vector2 worldCenter = b2dCmp.body.getWorldCenter();
        b2dCmp.body.applyLinearImpulse(
                (speedCmp.velocity.x - b2dCmp.body.getLinearVelocity().x) * b2dCmp.body.getMass(),
                (speedCmp.velocity.y - b2dCmp.body.getLinearVelocity().y) * b2dCmp.body.getMass(),
                worldCenter.x, worldCenter.y, true);

        if (currentTimeMillis - bulletCmp.startTime > 4000l) {
            entity.add(ecsEngine.createComponent(RemoveComponent.class));
        }

    }
}
