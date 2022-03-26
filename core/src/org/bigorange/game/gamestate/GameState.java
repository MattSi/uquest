package org.bigorange.game.gamestate;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import org.bigorange.game.utils.Utils;
import org.bigorange.game.input.KeyInputListener;

public abstract class GameState <T extends Table> implements Disposable, KeyInputListener {
    private final EGameState type;


    protected GameState(final EGameState type){
        this.type = type;
    }


    public EGameState getType() {
        return type;
    }

    public void activate(){
        /**
         * 1. set key input listener
         * 2. set gameStateHUD visible
         */
        Utils.getInputManager().addKeyInputListener(this);
    }

    public void deactivate(){
        Utils.getInputManager().removeKeyInputListener(this);
    }

    public void step(final float fixedTimeStep){
    }

    public void render(final float alpha){
    }

    public void resize(final int width, final int height){
    }
}
