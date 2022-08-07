package org.bigorange.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.ecs.component.CpuComponent;
import org.bigorange.game.ecs.component.PlayerComponent;

public class WorldContactManager implements ContactListener {
    private static final String TAG = WorldContactManager.class.getSimpleName();
    private final ComponentMapper<PlayerComponent> playerCmpMapper;
    private final ComponentMapper<CpuComponent> cpuCmpMapper;
    private final Array<WorldContactListener> listeners;
    private Entity player;
    private Entity cpuGameObj;
    private boolean isPlayerSensor;
    private boolean isGameObjSensor;

    public WorldContactManager(){
        playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
        cpuCmpMapper = ComponentMapper.getFor(CpuComponent.class);

        listeners = new Array<>();
        player = null;
        cpuGameObj = null;
        isPlayerSensor = false;
        isGameObjSensor = false;

    }
    public void addWorldContactListener(WorldContactListener listener){
        if(!listeners.contains(listener, true)){
            listeners.add(listener);
        }
    }

    private boolean prepareContactData(final Contact contact) {
        player = null;
        cpuGameObj = null;
        isPlayerSensor = false;

        Gdx.app.log(TAG, "--------------------------------------");
        final Object userDataA = contact.getFixtureA().getBody().getUserData();
        final Object userDataB = contact.getFixtureB().getBody().getUserData();

        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();

        if (!(userDataA instanceof Entity) || !(userDataB instanceof Entity)) {
            return false;
        }

        if (playerCmpMapper.get((Entity) userDataA) != null) {
            player = (Entity) userDataA;
            if(fixtureA.isSensor()){
                isPlayerSensor=true;
            }
        } else if (playerCmpMapper.get((Entity) userDataB) != null) {
            player = (Entity) userDataB;
            if(fixtureB.isSensor()){
                isPlayerSensor=true;
            }
        } else {
            return false;
        }

        if (cpuCmpMapper.get((Entity) userDataA) != null) {
            cpuGameObj = (Entity) userDataA;
            if (fixtureA.isSensor()) {
                isGameObjSensor = true;
            }
        } else if (cpuCmpMapper.get((Entity) userDataB) != null) {
            cpuGameObj = (Entity) userDataB;
            if (fixtureB.isSensor()) {
                isGameObjSensor = true;
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
            listener.beginContact(player, cpuGameObj, isPlayerSensor, isGameObjSensor);
        }

    }

    @Override
    public void endContact(Contact contact) {
        if(!prepareContactData(contact))
            return;

        for (final WorldContactListener listener : listeners) {
            listener.endContact(player, cpuGameObj, isPlayerSensor, isGameObjSensor);
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public interface WorldContactListener {
        void beginContact(final Entity player, final Entity cpuGameObj,
                          boolean isPlayerSensor, boolean isGameObjSensor);

        void endContact(final Entity player, final Entity cpuGameObj,
                        boolean isPlayerSensor, boolean isGameObjSensor);
    }
}
