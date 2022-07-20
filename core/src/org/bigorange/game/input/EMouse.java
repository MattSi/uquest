package org.bigorange.game.input;

import com.badlogic.gdx.Input;

public enum EMouse {
    LEFT(Input.Buttons.LEFT),
    MIDDLE(Input.Buttons.MIDDLE),
    RIGHT(Input.Buttons.RIGHT);

    final int mouseCode;

    public int getMouseCode() {
        return mouseCode;
    }

    EMouse(int mouseCode) {
        this.mouseCode = mouseCode;
    }
}
