package org.bigorange.game.gamestate;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.system.GameRenderSystem;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.ui.TTFSkin;
import org.bigorange.game.ui.GameUI;
import org.bigorange.game.utils.Utils;

public class GSGame extends GameState<GameUI>  {
    private final ECSEngine ecsEngine;

    public GSGame(final EGameState type) {
        super(type);

        // TODO init box2d

        // TODO init player light

        // TODO init entity component system
        this.ecsEngine = new ECSEngine(new OrthographicCamera());
    }

    @Override
    public void render(float alpha) {
        ecsEngine.render(alpha);
        super.render(alpha);
    }

    @Override
    public void activate() {
        super.activate();
       // Utils.getInputManager().addKeyInputListener(ecsEngine.getRenderSystem());
    }

    @Override
    public void dispose() {
        ecsEngine.dispose();
    }


    @Override
    public void step(float fixedTimeStep) {
        ecsEngine.update(fixedTimeStep);
        super.step(fixedTimeStep);
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {

    }

    @Override
    public void keyUP(InputManager manager, EKey key) {

    }

}
