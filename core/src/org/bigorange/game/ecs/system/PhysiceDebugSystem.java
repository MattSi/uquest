package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class PhysiceDebugSystem extends IteratingSystem {
    private Box2DDebugRenderer dbgRender;
    private World world;
    private OrthographicCamera camera;

    public PhysiceDebugSystem(World world, OrthographicCamera camera){
        super(Family.all().get());
        this.world = world;
        this.camera = camera;
        this.dbgRender = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        dbgRender.render(world, camera.combined);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
