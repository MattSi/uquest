package org.bigorange.game.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import org.bigorange.game.ecs.component.*;
import org.bigorange.game.ecs.system.RemoveSystem;

public abstract class EntityEngine extends PooledEngine implements Disposable {
    private static final String TAG = EntityEngine.class.getSimpleName();

    public static ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static ComponentMapper<AnimationSimpleComponent> aniCmpMapper = ComponentMapper.getFor(AnimationSimpleComponent.class);
    public static ComponentMapper<AnimationComponent> aniCmpMapper2 = ComponentMapper.getFor(AnimationComponent.class);
    public static ComponentMapper<Animation4DirectionsComponent> ani4dCmpMapper = ComponentMapper.getFor(Animation4DirectionsComponent.class);
    public static ComponentMapper<Box2DComponent> b2dCmpMapper = ComponentMapper.getFor(Box2DComponent.class);
    public static ComponentMapper<BulletComponent> bulletCmpMapper = ComponentMapper.getFor(BulletComponent.class);
    public static ComponentMapper<GameObjectComponent> gameObjCmpMapper = ComponentMapper.getFor(GameObjectComponent.class);
    public static ComponentMapper<GameObjectComponent2> gameObj2CmpMapper = ComponentMapper.getFor(GameObjectComponent2.class);
    public static ComponentMapper<CpuComponent> cpuCmpMapper = ComponentMapper.getFor(CpuComponent.class);
    public static ComponentMapper<SpeedComponent> speedCmpMapper = ComponentMapper.getFor(SpeedComponent.class);
    public static ComponentMapper<SteeringComponent> steerCmpMapper = ComponentMapper.getFor(SteeringComponent.class);
    public static ComponentMapper<SteeringLocationComponent> stLocationCmpMapper = ComponentMapper.getFor(SteeringLocationComponent.class);
    public static ComponentMapper<InteractComponent> interactCmpMapper = ComponentMapper.getFor(InteractComponent.class);
    public static ComponentMapper<ActionableComponent> actionCmpMapper = ComponentMapper.getFor(ActionableComponent.class);
    public static ComponentMapper<CpuCmpClosedToPlayerComponent> cpuCloseToPlayerComMapper = ComponentMapper.getFor(CpuCmpClosedToPlayerComponent.class);
    public static ComponentMapper<RollingComponent> rollingCmpMapper = ComponentMapper.getFor(RollingComponent.class);
    public static ComponentMapper<DeathClockComponent> deathClockCmpMapper = ComponentMapper.getFor(DeathClockComponent.class);

    private final Array<RenderSystem> renderSystems;
    public EntityEngine() {
        super();
        this.renderSystems = new Array<>();

        addSystem(new RemoveSystem());
    }

    public void addRenderSystem(final RenderSystem renderSystem) {
        if (!renderSystems.contains(renderSystem, true)) {
            renderSystems.add(renderSystem);
        }

    }


    public void render(final float alpha){
        for (RenderSystem renderSystem : renderSystems) {
            renderSystem.render(alpha);
        }

    }

    public void resize(final int width, final int height){
        for (RenderSystem renderSystem : renderSystems) {
            renderSystem.resize(width, height);
        }

    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "Disposing ECSEngine " + this);
        for (final RenderSystem renderSystem : renderSystems) {
            renderSystem.dispose();
        }

    }
}
