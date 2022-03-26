package org.bigorange.game.input;


public interface KeyInputListener {

    void keyDown(final InputManager manager, final EKey key);

    void keyUP(final InputManager manager, final EKey key);
}
