package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import com.badlogic.gdx.ai.msg.Telegraph;
import org.bigorange.game.message.MessageType;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.ActionableComponent;
import org.bigorange.game.ecs.component.InteractComponent;
import org.bigorange.game.ecs.component.RemoveComponent;

public class InteractSystem extends IteratingSystem implements TelegramProvider,Telegraph {
    public static final String TAG = InteractSystem.class.getSimpleName();

    public InteractSystem() {
        super(Family.all(InteractComponent.class).exclude(RemoveComponent.class).get());

        MessageManager.getInstance().addProvider(this, MessageType.MSG_PLAYER_TALK_TO_NPC);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final InteractComponent interactCmp = ECSEngine.interactCmpMapper.get(entity);

        if (interactCmp.interact && !interactCmp.entitiesInRange.isEmpty()) {
            final Entity closestEntity = interactCmp.getClosestEntity(entity);
            if (closestEntity != null) {
                doEntityAction(entity, closestEntity);
            }
        }

        interactCmp.interact = false;
    }

    private void doEntityAction(Entity player, Entity interactEntity) {
        final ActionableComponent actionTypeCmp = interactEntity.getComponent(ActionableComponent.class);
        switch (actionTypeCmp.type) {
            case TALK -> {
                Gdx.app.debug(TAG, "Talk.....");
                MessageManager.getInstance().dispatchMessage(0.2f, this, MessageType.MSG_PLAYER_TALK_TO_NPC, "Talk..");
            }
            case UNDEFINED -> {
            }
        }
    }

    @Override
    public Object provideMessageInfo(int msg, Telegraph receiver) {
        return this;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}
