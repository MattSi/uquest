package org.bigorange.game.gamestate;

import com.badlogic.gdx.maps.tiled.TiledMap;
import org.bigorange.game.ResourceManager;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.ui.HUD;
import org.bigorange.game.ui.LoadingUI;
import org.bigorange.game.ui.TTFSkin;
import org.bigorange.game.utils.Utils;

/**
 * 装入所有的资源
 */
public class GSLoading extends GameState<LoadingUI> {
    private final ResourceManager resourceManager;

    public GSLoading(EGameState type, HUD hud) {
        super(type, hud);

        resourceManager = Utils.getResourceManager();
        resourceManager.load("map/battle1.tmx", TiledMap.class);
    }

    @Override
    protected LoadingUI createHUD(HUD hud, TTFSkin skin) {
        return new LoadingUI(hud, skin);
    }

    @Override
    public void step(float fixedTimeStep) {
        resourceManager.update();

        // Set resource loading progress.
        gameStateHUD.setProgress(resourceManager.getProgress());
        super.step(fixedTimeStep);
    }

    @Override
    public void dispose() {
        hud.removeGameStateHUD(gameStateHUD);
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {
        if(resourceManager.getProgress() == 1){
            Utils.setGameState(EGameState.GAME, true);
        }
    }

    @Override
    public void keyUP(InputManager manager, EKey key) {

    }
}
