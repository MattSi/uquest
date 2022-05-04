package org.bigorange.game.core.gamestate;

import org.bigorange.game.gamestate.GSGame;
import org.bigorange.game.gamestate.GSLoading;

public enum EGameState {
    GAME(GSGame.class),
    LOADING(GSLoading.class);


    private final Class<? extends GameState> gsClass;

    EGameState(final Class<? extends GameState> gsClass){
        this.gsClass = gsClass;
    }

    public Class<? extends GameState> getGameStateType() {
        return gsClass;
    }
}
