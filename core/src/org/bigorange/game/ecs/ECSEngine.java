package org.bigorange.game.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.bigorange.game.ecs.component.UserMovementComponent;
import org.bigorange.game.ecs.system.GameRenderSystem;
import org.bigorange.game.ecs.system.PlayerAnimationSystem;
import org.bigorange.game.ecs.system.PlayerCameraSystem;
import org.bigorange.game.ecs.system.UserMovementSystem;

public class ECSEngine extends EntityEngine {
    private static final String TAG = ECSEngine.class.getSimpleName();

    public static final ComponentMapper<UserMovementComponent> usrMoveCmpMapper =
            ComponentMapper.getFor(UserMovementComponent.class);

    public ECSEngine(final OrthographicCamera gameCamera){
        addSystem(new UserMovementSystem());
        addSystem(new PlayerCameraSystem(gameCamera));
        addRenderSystem(new GameRenderSystem(gameCamera));

    }

    public void createTmpEntity(){
        final Entity user = createEntity();
        final UserMovementComponent usrMoveCmp = createComponent(UserMovementComponent.class);
        user.add(usrMoveCmp);

        addEntity(user);
    }
}
