package org.bigorange.game.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import org.bigorange.game.ecs.component.AnimationComponent;
import org.bigorange.game.ecs.component.Box2DComponent;
import org.bigorange.game.ecs.system.AnimationSystem;
import org.bigorange.game.ecs.system.RemoveSystem;

public abstract class EntityEngine extends PooledEngine implements Disposable {
    private static final String TAG = EntityEngine.class.getSimpleName();

    public static final ComponentMapper<AnimationComponent> aniCmpMapper =
            ComponentMapper.getFor(AnimationComponent.class);

    public static final ComponentMapper<Box2DComponent> b2dCmpMapper =
            ComponentMapper.getFor(Box2DComponent.class);

    private final Array<RenderSystem> renderSystems;
    public EntityEngine() {
        super();
        this.renderSystems = new Array<>();

        addSystem(new RemoveSystem());
        addSystem(new AnimationSystem());

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
