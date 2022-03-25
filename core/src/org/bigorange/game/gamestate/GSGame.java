package org.bigorange.game.gamestate;

import org.bigorange.game.core.gamestate.EGameState;
import org.bigorange.game.core.gamestate.GameState;
import org.bigorange.game.core.input.EKey;
import org.bigorange.game.core.input.InputManager;
import org.bigorange.game.core.ui.HUD;
import org.bigorange.game.core.ui.TTFSkin;
import org.bigorange.game.ui.GameUI;

public class GSGame extends GameState<GameUI>  {
    protected GSGame(final EGameState type, final HUD hud) {
        super(type, hud);
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

    @Override
    protected GameUI createHUD(HUD hud, TTFSkin skin) {
        return null;
    }
}
