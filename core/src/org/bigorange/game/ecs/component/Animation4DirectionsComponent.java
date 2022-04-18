package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;

public class Animation4DirectionsComponent implements Component, Pool.Poolable {
    public Animation<Sprite> aniLeft;
    public Animation<Sprite> aniRight;
    public Animation<Sprite> aniUp;
    public Animation<Sprite> aniDown;


    @Override
    public void reset() {
        aniDown = aniLeft = aniRight = aniUp = null;
    }
}
