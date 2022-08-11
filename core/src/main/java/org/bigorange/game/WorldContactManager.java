package org.bigorange.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.ecs.component.BulletComponent;
import org.bigorange.game.ecs.component.CpuComponent;
import org.bigorange.game.ecs.component.PlayerComponent;

public class WorldContactManager implements ContactListener {
    private static final String TAG = WorldContactManager.class.getSimpleName();
    private final ComponentMapper<PlayerComponent> playerCmpMapper;
    private final ComponentMapper<CpuComponent> cpuCmpMapper;
    private final ComponentMapper<BulletComponent> bulletCmpMapper;
    private final Array<WorldPlayerContactListener> playerContactListeners;
    private final Array<WorldBulletContactListener> bulletContactListeners;
    private Entity playerObj;
    private Entity playerContactObj;
    private boolean isPlayerSensor;
    private boolean isGameObjSensor;
    private Entity bulletObj;
    private Entity bulletContactObj;

    public WorldContactManager(){
        playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
        cpuCmpMapper = ComponentMapper.getFor(CpuComponent.class);
        bulletCmpMapper = ComponentMapper.getFor(BulletComponent.class);

        playerContactListeners = new Array<>();
        bulletContactListeners = new Array<>();
        playerObj = null;
        playerContactObj = null;
        bulletObj = null;
        bulletContactObj = null;
        isPlayerSensor = false;
        isGameObjSensor = false;

    }
    public void addWorldPlayerContactListener(WorldPlayerContactListener listener){
        if(!playerContactListeners.contains(listener, true)){
            playerContactListeners.add(listener);
        }
    }

    public void addWorldBulletContactListener(WorldBulletContactListener listener){
        if(!bulletContactListeners.contains(listener, true)) {
            bulletContactListeners.add(listener);
        }
    }

    private boolean prepareBulletContactData(final Contact contact){
        final Object userDataA = contact.getFixtureA().getBody().getUserData();
        final Object userDataB = contact.getFixtureB().getBody().getUserData();

        if(!(userDataA instanceof Entity) || !(userDataB instanceof Entity)){
            return false;
        }
        if(bulletCmpMapper.get((Entity) userDataA) != null){
            bulletObj = (Entity) userDataA;
            bulletContactObj = (Entity) userDataB;
        } else if(bulletCmpMapper.get((Entity) userDataB) != null){
            bulletObj = (Entity) userDataB;
            bulletContactObj = (Entity) userDataA;
        } else {
            return false;
        }

        return true;
    }

    private boolean preparePlayerContactData(final Contact contact) {
        playerObj = null;
        playerContactObj = null;
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
            playerObj = (Entity) userDataA;
            if(fixtureA.isSensor()){
                isPlayerSensor=true;
            }
        } else if (playerCmpMapper.get((Entity) userDataB) != null) {
            playerObj = (Entity) userDataB;
            if(fixtureB.isSensor()){
                isPlayerSensor=true;
            }
        } else {
            return false;
        }

        if (cpuCmpMapper.get((Entity) userDataA) != null) {
            playerContactObj = (Entity) userDataA;
            if (fixtureA.isSensor()) {
                isGameObjSensor = true;
            }
        } else if (cpuCmpMapper.get((Entity) userDataB) != null) {
            playerContactObj = (Entity) userDataB;
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
        if(preparePlayerContactData(contact)) {
            for (final WorldPlayerContactListener listener : playerContactListeners) {
                listener.beginContact(playerObj, playerContactObj, isPlayerSensor, isGameObjSensor);
            }
        }

        if(prepareBulletContactData(contact)) {
            for (WorldBulletContactListener listener : bulletContactListeners) {
                listener.beginContact(bulletObj, bulletContactObj);
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
        if(preparePlayerContactData(contact)) {
            for (final WorldPlayerContactListener listener : playerContactListeners) {
                listener.endContact(playerObj, playerContactObj, isPlayerSensor, isGameObjSensor);
            }
        }

        if(prepareBulletContactData(contact)) {
            for (WorldBulletContactListener listener : bulletContactListeners) {
                listener.endContact(bulletObj, bulletContactObj);
            }


        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public interface WorldPlayerContactListener {
        void beginContact(final Entity player, final Entity cpuGameObj,
                          boolean isPlayerSensor, boolean isGameObjSensor);

        void endContact(final Entity player, final Entity cpuGameObj,
                        boolean isPlayerSensor, boolean isGameObjSensor);

    }

    public interface WorldBulletContactListener {
        void beginContact(final Entity bullet, final Entity bulletContact);
        void endContact(final Entity bullet, final Entity bulletContact);
    }
}
