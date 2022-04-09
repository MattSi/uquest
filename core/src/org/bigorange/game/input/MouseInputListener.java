package org.bigorange.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public interface MouseInputListener {
    void buttonDown(final InputManager manager, final EMouse mouse, final Vector2 screenPoint);

    void buttonUp(final InputManager manager, final EMouse mouse, final Vector2 screenPoint);

    void mouseMoved(final InputManager manager, final Vector2 screenPoint);
}
