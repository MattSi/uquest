package org.bigorange.game.core.gamestate;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import org.bigorange.game.core.input.KeyInputListener;
import org.bigorange.game.core.ui.HUD;
import org.bigorange.game.core.ui.TTFSkin;

public abstract class GameState <T extends Table> implements Disposable, KeyInputListener {
    private final EGameState type;
    protected final HUD hud;
    protected final T gameStateHUD;


    protected GameState(final EGameState type, final HUD hud){
        this.type = type;
        this.hud = hud;
        gameStateHUD = createHUD(hud, hud.getSkin());
        gameStateHUD.setVisible(false);
    }

    protected abstract T createHUD(final HUD hud, final TTFSkin skin);

    public EGameState getType() {
        return type;
    }

    public void activate(){

    }

    public void deactivate(){

    }

    public void step(final float fixedTimeStep){

    }

    public void render(final float alpha){

    }

    public void resize(final int width, final int height){

    }
}
