package org.bigorange.game.core.gamestate;

import org.bigorange.game.gamestate.GSGame;

public enum EGameState {
    GAME(GSGame.class);


    private final Class<? extends GSGame> gsClass;

    EGameState(final Class<? extends GSGame> gsClass){
        this.gsClass = gsClass;
    }

    public Class<? extends GSGame> getGsClass() {
        return gsClass;
    }
}
