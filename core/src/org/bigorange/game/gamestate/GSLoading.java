package org.bigorange.game.gamestate;

import com.badlogic.gdx.Gdx;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.assets.MapAssets;
import org.bigorange.game.assets.TextureAtlasAssets;
import org.bigorange.game.core.assets.LocaleAssets;
import org.bigorange.game.core.gamestate.EGameState;
import org.bigorange.game.core.gamestate.GameState;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.ui.HUD;
import org.bigorange.game.ui.LoadingUI;
import org.bigorange.game.ui.TTFSkin;
import org.bigorange.game.core.Utils;

/**
 * 装入所有的资源
 */
public class GSLoading extends GameState<LoadingUI> {
    private final ResourceManager resourceManager;

    public GSLoading(EGameState type, HUD hud) {
        super(type, hud);

        resourceManager = Utils.getResourceManager();

        // Load Map
        for (MapAssets item : MapAssets.values()) {
            resourceManager.load(item.getLocation(), MapAssets.klass);
        }

        // Load Texture atlas
        for (TextureAtlasAssets item : TextureAtlasAssets.values()) {
            resourceManager.load(item.getLocation(), TextureAtlasAssets.klass);
        }

        Gdx.app.debug("GSLoading", LocaleAssets.getLocales().toString());
    }

    @Override
    protected LoadingUI createGameStateUI(HUD hud, TTFSkin skin) {
        return new LoadingUI(hud, skin);
    }

    @Override
    public void step(float fixedTimeStep) {
        resourceManager.update();

        // Set resource loading progress.
        gameStateUI.setProgress(resourceManager.getProgress());
        super.step(fixedTimeStep);
    }

    @Override
    public void dispose() {
        hud.removeGameStateHUD(gameStateUI);
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {
        if(resourceManager.getProgress() == 1){
            Utils.setGameState(EGameState.MENU, true);
        }
    }

    @Override
    public void keyUP(InputManager manager, EKey key) {

    }
}
