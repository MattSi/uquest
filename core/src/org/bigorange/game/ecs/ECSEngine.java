package org.bigorange.game.ecs;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.bigorange.game.ecs.system.GameRenderSystem;
import org.bigorange.game.ecs.system.PlayerAnimationSystem;

public class ECSEngine extends EntityEngine {
    private static final String TAG = ECSEngine.class.getSimpleName();


    public ECSEngine(final OrthographicCamera gameCamera){
        addRenderSystem(new GameRenderSystem(gameCamera));
    }
}
