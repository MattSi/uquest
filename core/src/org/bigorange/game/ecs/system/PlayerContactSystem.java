package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import org.bigorange.game.WorldContactManager;

public class PlayerContactSystem extends EntitySystem implements WorldContactManager.WorldContactListener {

    @Override
    public void beginContact(Entity player, Entity gameObject) {

    }
}
