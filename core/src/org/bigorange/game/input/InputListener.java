package org.bigorange.game.input;


import com.badlogic.gdx.math.Vector2;

public interface InputListener {

    void keyDown(final InputManager manager, final EKey key);

    void keyUP(final InputManager manager, final EKey key);


    void buttonDown(final InputManager manager, final EMouse mouse, final Vector2 screenPoint, int pointer);

    void buttonUp(final InputManager manager, final EMouse mouse, final Vector2 screenPoint, int pointer);

    void mouseMoved(final InputManager manager, final Vector2 screenPoint);

    void setEnabled(boolean enabled);
    boolean isEnabled();
}
