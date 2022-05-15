package org.bigorange.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import org.bigorange.game.core.ui.BaseUI;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class LoadingUI extends BaseUI {
    private final TextButton pressAnyButtonInfo;
    private final ProgressBar progressBar;
    private final TextButton note;



    public LoadingUI(final HUD hud, final TTFSkin skin){
        super(skin);

        progressBar = new ProgressBar(0, 1, 0.01f, false, skin, "default");

        pressAnyButtonInfo = new TextButton(hud.getLocalizedString("pressAnyKey"), skin, "huge");
        pressAnyButtonInfo.setVisible(false);
        pressAnyButtonInfo.getLabel().setWrap(true);

        note = new TextButton(hud.getLocalizedString("loading"), skin, "normal");

        add(new Image(skin.getDrawable("banner"))).expand().top().padTop(65).row();
        add(pressAnyButtonInfo).expand().fillX().center().row();
        add(note).expand().fillX().bottom().row();
        add(progressBar).expandX().fillX().pad(15, 50, 175, 50).bottom();
        //debugAll();
    }

    public void setProgress(final float progress, final HUD hud){
        progressBar.setValue(progress);
        if(progress >=1 && !pressAnyButtonInfo.isVisible()){
            pressAnyButtonInfo.setVisible(true);
            pressAnyButtonInfo.setColor(1,1,1,0);
            pressAnyButtonInfo.addAction(forever(sequence(alpha(1,1), alpha(0, 1))));

            note.setText(hud.getLocalizedString("loadFinish"));
        }
    }
}
