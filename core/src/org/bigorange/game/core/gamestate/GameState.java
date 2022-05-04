package org.bigorange.game.core.gamestate;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import org.bigorange.game.ui.HUD;
import org.bigorange.game.ui.TTFSkin;
import org.bigorange.game.utils.Utils;
import org.bigorange.game.input.KeyInputListener;

public abstract class GameState <T extends Table> implements Disposable, KeyInputListener {
    private final EGameState type;
    protected final HUD hud;
    protected final T gameStateUI;


    protected GameState(final EGameState type, final HUD hud){
        this.type = type;
        this.hud = hud;

        gameStateUI = createGameStateUI(hud, hud.getSkin());
        hud.addGameStateHUD(gameStateUI);
        gameStateUI.setVisible(false);
    }

    protected abstract T createGameStateUI(final HUD hud, final TTFSkin skin);
    public EGameState getType() {
        return type;
    }

    public void activate(){
        /**
         * 1. set key input listener
         * 2. set gameStateHUD visible
         */
        Utils.getInputManager().addKeyInputListener(this);
        gameStateUI.setVisible(true);
    }

    public void deactivate(){
        Utils.getInputManager().removeKeyInputListener(this);
        gameStateUI.setVisible(false);
    }

    public void step(final float fixedTimeStep){
        hud.step(fixedTimeStep);
    }

    public void render(final float alpha){
        hud.render(alpha);
    }

    public void resize(final int width, final int height){
        hud.resize(width, height);
    }
}
