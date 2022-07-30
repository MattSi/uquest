package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;

public class InteractComponent implements Component, Pool.Poolable{
    public ObjectSet<Entity> entitiesInRange =  new ObjectSet<Entity>();
    public boolean interact = false;

    private Vector2 TMP_VECTOR_1 = new Vector2();
    private Vector2 TMP_VECTOR_2 = new Vector2();

    public Entity getClosestEntity(Entity self){
        if(entitiesInRange.isEmpty()){
            return null;
        }

        final Box2DComponent selfB2dCmp = self.getComponent(Box2DComponent.class);
        final Vector2 selfPosition = selfB2dCmp.body.getPosition();
        TMP_VECTOR_1.set(selfPosition);

        Entity closestEntity = entitiesInRange.first();
        float lastDistance = -1f;

        for (Entity entity : entitiesInRange.iterator()) {
            final Box2DComponent b2dCmp = entity.getComponent(Box2DComponent.class);
            final Vector2 p = b2dCmp.body.getPosition();
            TMP_VECTOR_2.set(p);
            final float distance = TMP_VECTOR_1.dst2(TMP_VECTOR_2);

            if(lastDistance == -1f || lastDistance > distance){
                lastDistance = distance;
                closestEntity = entity;
            }
        }

        return closestEntity;
    }

    public void addInRangeEntity(Entity entity){
        entitiesInRange.add(entity);
    }

    public void removeInRangeEntity(Entity entity){
        entitiesInRange.remove(entity);
    }


    @Override
    public void reset() {
        entitiesInRange.clear();
        TMP_VECTOR_1.setZero();
        TMP_VECTOR_2.setZero();
        interact = false;
    }
}
