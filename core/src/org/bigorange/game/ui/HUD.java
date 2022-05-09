package org.bigorange.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.input.KeyInputListener;
import org.bigorange.game.core.Utils;

import static org.bigorange.game.Game.TARGET_FRAME_TIME;

/**
 * 抬头显示设备(抽象)， 主要存放一些游戏界面，比如菜单，游戏中的物品栏等等。
 *
 * 一些资源的存放处
 * ps. 手机游戏中的虚拟按键也可以在这里定义。
 */
public class HUD extends InputListener implements Disposable, KeyInputListener {
    private static final String TAG = HUD.class.getSimpleName();

    private final Stage stage;
    private final TTFSkin skin;
    private final I18NBundle i18NBundle;
    private final Stack gameStateHUDs;


    public HUD() {
        stage = new Stage(
                new FitViewport(
                        Gdx.graphics.getWidth(),
                        Gdx.graphics.getHeight(),
                        new OrthographicCamera()),
                Utils.getSpriteBatch());

        gameStateHUDs = new Stack();
        gameStateHUDs.setFillParent(true);
        this.stage.addActor(gameStateHUDs);

        final ResourceManager resourceManager = Utils.getResourceManager();
        resourceManager.load("i18n/strings_zh_CN", I18NBundle.class);
        skin = resourceManager.loadSkinSynchronously("hud/hud.json", "hud/simfang.ttf", 16, 20, 26,32);
        i18NBundle = resourceManager.get("i18n/strings_zh_CN", I18NBundle.class);

//        final FileHandle fi = Gdx.files.internal("i18n/strings");
//        i18NBundle = I18NBundle.createBundle(fi, new Locale("zh", "CN"));
        stage.addListener(this);
    }

    public String getLocalizedString(final String key){
        return i18NBundle.format(key);
    }

    public void addGameStateHUD(final Table table){
        gameStateHUDs.add(table);
    }

    public void removeGameStateHUD(final Table table){
        gameStateHUDs.removeActor(table);
    }

    public void step(final float deltaTime){
        stage.act(deltaTime);
    }

    public void render(final float alpha){
        stage.act(alpha * TARGET_FRAME_TIME);
        stage.getViewport().apply();
        stage.draw();
    }
    public void resize(final int width, final int height){
        Gdx.app.debug(TAG, "Resize HUD to " + width + "x" + height);
        this.stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "Disposing HUD");
        stage.dispose();
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {
    }

    @Override
    public void keyUP(InputManager manager, EKey key) {
    }

    public Stage getStage() {
        return stage;
    }

    public TTFSkin getSkin() {
        return skin;
    }
}
