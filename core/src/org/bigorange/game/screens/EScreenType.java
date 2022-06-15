package org.bigorange.game.screens;

import org.bigorange.game.core.screens.BaseScreen;

public enum EScreenType {
    LOADING(LoadingScreen.class),

    MENU(MenuScreen.class),

    GAME(GameScreen.class);

    private final Class<? extends BaseScreen> screenClass;

    EScreenType(Class<? extends BaseScreen> screenClass) {
        this.screenClass = screenClass;
    }

    public Class<? extends BaseScreen> getScreenType() {
        return screenClass;
    }
}
