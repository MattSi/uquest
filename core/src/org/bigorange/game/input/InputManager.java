package org.bigorange.game.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

public class InputManager implements InputProcessor {
    private final EKey[] keyMapping;
    private final boolean[] keyState;
    private final Array<KeyInputListener> listeners;

    public InputManager() {
        this.keyMapping = new EKey[256];

        for (EKey key : EKey.values()) {
            for (int keyCode : key.keyCode) {
                keyMapping[keyCode] = key;
            }
        }
        this.keyState = new boolean[EKey.values().length];
        listeners = new Array<>();

    }

    public void addKeyInputListener(KeyInputListener listener) {
        if(!listeners.contains(listener, true)){
            listeners.add(listener);
        }
    }

    public void removeKeyInputListener(KeyInputListener listener){
        listeners.removeValue(listener, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        final EKey key = keyMapping[keycode];
        if( key == null) {
            return false;
        }
        notifyKeyDown(key);
        return true;
    }

    @Override
    public boolean keyUp(final int keycode) {
        final EKey key = keyMapping[keycode];
        if(key == null){
            // no relevant key for game
            return false;
        }

        notifyKeyUp(key);
        return true;
    }

    public void notifyKeyDown(final EKey key){
        keyState[key.ordinal()] = true;
        for (final KeyInputListener listener : listeners) {
            listener.keyDown(this, key);
        }

    }

    public void notifyKeyUp(final EKey key) {
        keyState[key.ordinal()] = false;
        for (final KeyInputListener listener : listeners) {
            listener.keyUP(this, key);
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
