package org.bigorange.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class InputManager implements InputProcessor {
    private static final String TAG = InputManager.class.getSimpleName();
    private final EKey[] keyMapping;
    private final boolean[] keyState;

    private final EMouse[] mouseMapping;
    private final boolean[] buttonState;
    private final Array<KeyInputListener> keyInputListeners;
    private final Array<MouseInputListener> mouseInputListeners;
    private final Vector2 screenPoint;

    public InputManager() {
        this.keyMapping = new EKey[256];
        this.mouseMapping = new EMouse[5];
        this.screenPoint = new Vector2(0,0);

        for (EKey key : EKey.values()) {
            for (int keyCode : key.keyCode) {
                keyMapping[keyCode] = key;
            }
        }

        for (EMouse key : EMouse.values()) {
            mouseMapping[key.mouseCode] = key;
        }

        this.keyState = new boolean[EKey.values().length];
        this.buttonState = new boolean[EMouse.values().length]; // number of static final fields in class Buttons is 5 (com.badlogic.gdx.Input.Buttons)
        keyInputListeners = new Array<>();
        mouseInputListeners = new Array<>();

    }

    public void addKeyInputListener(KeyInputListener listener) {
        if(!keyInputListeners.contains(listener, true)){
            keyInputListeners.add(listener);
        }
    }

    public void removeKeyInputListener(KeyInputListener listener){
        keyInputListeners.removeValue(listener, true);
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

    public boolean isKeyDown(final EKey key){
        return keyState[key.ordinal()];
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
        for (final KeyInputListener listener : keyInputListeners) {
            listener.keyDown(this, key);
        }

    }

    public void notifyKeyUp(final EKey key) {
        keyState[key.ordinal()] = false;
        for (final KeyInputListener listener : keyInputListeners) {
            listener.keyUP(this, key);
        }
    }

    public void addMouseInputListener(MouseInputListener listener){
        if(mouseInputListeners.contains(listener,true))
            return;
        mouseInputListeners.add(listener);
    }

    public void removeMouseInputListener(MouseInputListener listener){
        mouseInputListeners.removeValue(listener, true);
    }

    public boolean isMouseDown(EMouse mouse) {
        return buttonState[mouse.ordinal()];
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        buttonState[button] = true;
        EMouse mouse = mouseMapping[button];
        for (final MouseInputListener listener : mouseInputListeners) {
            listener.buttonDown(this, mouse, new Vector2(screenX, screenY));
            //Gdx.app.debug(TAG, "Click: " + listener);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        buttonState[button] = false;
        EMouse mouse = mouseMapping[button];
        for (final MouseInputListener listener : mouseInputListeners) {
            listener.buttonUp(this, mouse, new Vector2(screenX, screenY));
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        for (final MouseInputListener listener : mouseInputListeners) {
            listener.mouseMoved(this, this.screenPoint.set(screenX, screenY));
        }

        return true;
    }
    //////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }



    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
