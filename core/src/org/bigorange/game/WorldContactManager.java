package org.bigorange.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.ecs.component.GameObjectComponent;
import org.bigorange.game.ecs.component.PlayerComponent;

public class WorldContactManager implements ContactListener {
    private final ComponentMapper<PlayerComponent> playerCmpMapper;
    private final ComponentMapper<GameObjectComponent> gameObjectCmpMapper;
    private final Array<WorldContactListener> listeners;
    private Entity player;
    private Entity gameObj;
    private boolean isSensor;

    public WorldContactManager(){
        playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
        gameObjectCmpMapper = ComponentMapper.getFor(GameObjectComponent.class);
        listeners = new Array<>();
        player = null;
        gameObj = null;
        isSensor = false;

    }
    public void addWorldContactListener(WorldContactListener listener){
        if(!listeners.contains(listener, true)){
            listeners.add(listener);
        }
    }

    private boolean prepareContactData(final Contact contact) {
        player = null;
        gameObj = null;
        isSensor = false;

        final Object userDataA = contact.getFixtureA().getBody().getUserData();
        final Object userDataB = contact.getFixtureB().getBody().getUserData();

        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();

        if (!(userDataA instanceof Entity) || !(userDataB instanceof Entity)) {
            return false;
        }

        if (playerCmpMapper.get((Entity) userDataA) != null) {
            player = (Entity) userDataA;
        } else if (playerCmpMapper.get((Entity) userDataB) != null) {
            player = (Entity) userDataB;
        } else {
            return false;
        }

        if (gameObjectCmpMapper.get((Entity) userDataA) != null) {
            gameObj = (Entity) userDataA;
            if (fixtureA.isSensor()) {
                isSensor = true;
            }
        } else if (gameObjectCmpMapper.get((Entity) userDataB) != null) {
            gameObj = (Entity) userDataB;
            if (fixtureB.isSensor()) {
                isSensor = true;
            }
        } else {
            return false;
        }

        return true;
    }

    @Override
    public void beginContact(Contact contact) {
        if(!prepareContactData(contact))
            return;

        for (final WorldContactListener listener : listeners) {
            listener.beginContact(player, gameObj, isSensor);
        }

    }

    @Override
    public void endContact(Contact contact) {
        if(!prepareContactData(contact))
            return;

        for (final WorldContactListener listener : listeners) {
            listener.endContact(player, gameObj, isSensor);
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public interface WorldContactListener {
        void beginContact(final Entity player, final Entity gameObject, boolean isSensor);

        void endContact(final Entity player, final Entity gameObject, boolean isSensor);
    }
}
