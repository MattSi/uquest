package org.bigorange.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public interface MouseInputListener {
    void buttonDown(final InputManager manager, final EMouse mouse, final Vector2 vector2);

    void buttonUp(final InputManager manager, final EMouse mouse, final Vector2 vector2);
}
