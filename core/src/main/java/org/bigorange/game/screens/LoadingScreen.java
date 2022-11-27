package org.bigorange.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.bigorange.game.ResourceManager;
import org.bigorange.game.UndergroundQuest;
import org.bigorange.game.Utils;
import org.bigorange.game.assets.LocaleAssets;
import org.bigorange.game.assets.MapAsset;
import org.bigorange.game.assets.MusicAsset;
import org.bigorange.game.assets.TextureAtlasAssets;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.ui.TTFSkin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class LoadingScreen extends BaseScreen  {
    private ResourceManager resourceManager;
    private boolean isMusicLoaded;

    private TextButton pressAnyButtonInfo;
    private ProgressBar progressBar;
    private TextButton note;
    private Table table;

    public LoadingScreen(TTFSkin skin, ScreenManager screenManager){
        super(skin, screenManager);
        resourceManager = Utils.getResourceManager();
        isMusicLoaded = false;

        table = new Table();
        table.setFillParent(false);
        table.setPosition(100,20);
        progressBar = new ProgressBar(0, 1, 0.01f, false, skin, "green_h");

        pressAnyButtonInfo = new TextButton("Press Any Key", skin, "huge");
        pressAnyButtonInfo.setVisible(false);
        pressAnyButtonInfo.getLabel().setWrap(false);

        note = new TextButton("Loading...", skin, "normal");

        //table.add(new Image(skin.getDrawable("banner"))).expand().top().padTop(65).row();
        table.add(pressAnyButtonInfo).expand().fillX().center().row();
        table.add(note).expand().fillX().bottom().row();
        //table.add(progressBar).expandX().fillX().pad(15, 50, 175, 50).bottom();
        table.add(progressBar).expandX().fillX().bottom();

        pressAnyButtonInfo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                return false;
            }
        });

        stage.addActor(table);
        stage.setDebugAll(true);
    }

    @Override
    public void show() {
        super.show();

        // Load Map
        for (MapAsset item : MapAsset.values()) {
            resourceManager.load(item.getFilePath(), MapAsset.klass);
        }

        // Load Texture atlas
        for (TextureAtlasAssets item : TextureAtlasAssets.values()) {
            resourceManager.load(item.getFilePath(), TextureAtlasAssets.klass);
        }

        // Load Music
        for (MusicAsset item : MusicAsset.values()) {
            resourceManager.load(item.getFilePath(), MusicAsset.klass);
        }


        Gdx.app.debug(TAG, LocaleAssets.getLocales().toString());
    }

    @Override
    public void render(float delta) {

        resourceManager.update();

        setProgress(resourceManager.getProgress());
        if(!isMusicLoaded && resourceManager.isLoaded(MusicAsset.INTRO.getFilePath())){
            isMusicLoaded = true;
            Utils.getAudioManager().playMusic(MusicAsset.INTRO);
        }

        super.render(delta);

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Gdx.app.debug(TAG, "Resize HUD to " + width + "x" + height);
        stage.getViewport().update(width, height);
    }

    public void setProgress(final float progress) {
        progressBar.setValue(progress);
        if (progress >= 1 && !pressAnyButtonInfo.isVisible()) {
            pressAnyButtonInfo.setVisible(true);
            pressAnyButtonInfo.setColor(1, 1, 1, 0);
            pressAnyButtonInfo.addAction(forever(sequence(alpha(1, 1), alpha(0, 1))));

            note.setText("Load Finish.");
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        Gdx.app.debug(TAG, "Disposing...");
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {
        if(resourceManager.getProgress() == 1){
            final UndergroundQuest gameInstance = Utils.getGameInstance();
            screenManager.setScreenType(EScreenType.MENU);

        }
    }
}
