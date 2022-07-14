package org.bigorange.game.gameobjs;

import com.badlogic.gdx.math.MathUtils;

public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    static public Direction getRandomNext() {
        return Direction.values()[MathUtils.random(Direction.values().length - 1)];
    }

    public Direction getOpposite() {

        return null;
    }
}
