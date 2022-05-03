package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import org.bigorange.game.core.ActionType;

public class ActionableComponent implements Component, Pool.Poolable {
    public ActionType type = ActionType.UNDEFINED;

    @Override
    public void reset() {
        type = ActionType.UNDEFINED;
    }
}

