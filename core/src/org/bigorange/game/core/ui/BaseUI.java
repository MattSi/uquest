package org.bigorange.game.core.ui;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.bigorange.game.ui.TTFSkin;

public abstract class BaseUI extends Table implements Telegraph {

    protected final TTFSkin skin;

    public BaseUI(TTFSkin skin){
        this.skin = skin;
    }
    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}
