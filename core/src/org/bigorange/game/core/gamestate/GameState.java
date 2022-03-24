package org.bigorange.game.core.gamestate;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import org.bigorange.game.core.input.KeyInputListener;

public abstract class GameState <T extends Table> implements Disposable, KeyInputListener {
    private final EGameState type;


    protected GameState(final EGameState type){
        this.type = type;
    }

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
