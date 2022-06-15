package org.bigorange.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.I18NBundle;
import org.bigorange.game.core.Utils;
import org.bigorange.game.core.dialogue.ConversationManager;
import org.bigorange.game.core.dialogue.DialogueNode;
import org.bigorange.game.core.message.MessageType;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.component.ActionableComponent;
import org.bigorange.game.ecs.component.InteractComponent;
import org.bigorange.game.ecs.component.RemoveComponent;

public class InteractSystem extends IteratingSystem implements TelegramProvider,Telegraph {
    public static final String TAG = InteractSystem.class.getSimpleName();
    private final I18NBundle i18NBundle;
    private DialogueNode dialogueNode;

    public InteractSystem() {
        super(Family.all(InteractComponent.class).exclude(RemoveComponent.class).get());

        MessageManager.getInstance().addProvider(this, MessageType.MSG_NPC_TALK_TO_PLAYER);
        i18NBundle = Utils.getResourceManager().get("i18n/strings_zh_CN", I18NBundle.class);
        dialogueNode = null;
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
                final ConversationManager conversationManager = Utils.getConversationManager();
                dialogueNode = conversationManager.talk(1);
                MessageManager.getInstance().dispatchMessage(0.2f, this, MessageType.MSG_NPC_TALK_TO_PLAYER, dialogueNode);
            }
            case UNDEFINED -> {
            }
        }
    }

    @Override
    public Object provideMessageInfo(int msg, Telegraph receiver) {
        return dialogueNode;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}
