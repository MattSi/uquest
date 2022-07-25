package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

public class RollingComponent implements Component, Pool.Poolable {
    public static final float rollingTime = 0.15f; // 0.15 second
    public float deltaTime = 0f;
    public boolean isTriggered = false;
    public Color rollingColor = RollingColor.C1.color;
    public boolean isRolling;
    @Override
    public void reset() {
        deltaTime = 0f;
        isTriggered = false;
        rollingColor = RollingColor.C1.color;
        isRolling = false;
    }

    public boolean isTimeUp() {
        return deltaTime > rollingTime;
    }

    public static enum RollingColor {
        C1(Color.DARK_GRAY),
        C2(Color.BLUE),
        C3(Color.NAVY),
        C4(Color.ROYAL),
        C5(Color.TEAL),
        C6(Color.GREEN),
        C7(Color.LIME),
        C8(Color.GOLD),
        C9(Color.ORANGE),
        C10(Color.TAN),
        C11(Color.CORAL),
        C12(Color.SALMON),
        C13(Color.MAGENTA),
        C14(Color.PURPLE),
        C15(Color.VIOLET),
        C16(Color.MAROON);

        private Color color;

        public Color getColor() {
            return color;
        }

        RollingColor(Color color) {
            this.color = color;
        }
    }
}
