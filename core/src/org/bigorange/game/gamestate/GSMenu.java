package org.bigorange.game.gamestate;

import org.bigorange.game.core.gamestate.EGameState;
import org.bigorange.game.core.gamestate.GameState;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.ui.HUD;
import org.bigorange.game.ui.MenuUI;
import org.bigorange.game.ui.TTFSkin;

public class GSMenu extends GameState<MenuUI> {
    private int changeVolume;

    public GSMenu(final EGameState type, final HUD hud){
        super(type, hud);
        changeVolume = 0;
    }

    @Override
    protected MenuUI createGameStateUI(HUD hud, TTFSkin skin) {
        return new MenuUI(hud, skin, 0);
    }

    @Override
    public void activate() {
        super.activate();
        //gameStateUI.

    }

    @Override
    public void step(float fixedTimeStep) {
        super.step(fixedTimeStep);
        if (changeVolume != 0) {
            if(changeVolume >0){
                //gameStateUI.mo
            }
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void keyDown(InputManager manager, EKey key) {

    }

    @Override
    public void keyUP(InputManager manager, EKey key) {

    }
}
