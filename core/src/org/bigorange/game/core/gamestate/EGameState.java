package org.bigorange.game.core.gamestate;

import org.bigorange.game.gamestate.GSGame;
import org.bigorange.game.gamestate.GSLoading;
import org.bigorange.game.gamestate.GSMenu;

public enum EGameState {
    GAME(GSGame.class),
    LOADING(GSLoading.class),
    MENU(GSMenu.class);


    private final Class<? extends GameState> gsClass;

    EGameState(final Class<? extends GameState> gsClass){
        this.gsClass = gsClass;
    }

    public Class<? extends GameState> getGameStateType() {
        return gsClass;
    }
}
