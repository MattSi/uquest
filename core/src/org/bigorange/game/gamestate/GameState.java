package org.bigorange.game.gamestate;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import org.bigorange.game.ui.HUD;
import org.bigorange.game.ui.TTFSkin;
import org.bigorange.game.utils.Utils;
import org.bigorange.game.input.KeyInputListener;

public abstract class GameState <T extends Table> implements Disposable, KeyInputListener {
    private final EGameState type;
    protected final HUD hud;
    protected final T gameStateHUD;


    protected GameState(final EGameState type, final HUD hud){
        this.type = type;
        this.hud = hud;

        gameStateHUD = createHUD(hud, hud.getSkin());
        hud.addGameStateHUD(gameStateHUD);
        gameStateHUD.setVisible(false);
    }

    protected abstract T createHUD(final HUD hud, final TTFSkin skin);
    public EGameState getType() {
        return type;
    }

    public void activate(){
        /**
         * 1. set key input listener
         * 2. set gameStateHUD visible
         */
        Utils.getInputManager().addKeyInputListener(this);
        gameStateHUD.setVisible(true);
    }

    public void deactivate(){
        Utils.getInputManager().removeKeyInputListener(this);
        gameStateHUD.setVisible(false);
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
