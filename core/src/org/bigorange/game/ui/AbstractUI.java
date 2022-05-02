package org.bigorange.game.ui;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class AbstractUI extends Table implements Telegraph {

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}
