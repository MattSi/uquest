package org.bigorange.game.gameobjs;

import com.badlogic.gdx.math.MathUtils;

public enum State {
    IDLE,
    WALKING,
    IMMOBILE;

    static public State getRandomNext() {
        // Ignore IMMOBILE which should be last state
        return State.values()[MathUtils.random(State.values().length - 2)];
    }
}
