package org.bigorange.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.UndergroundQuest;
import org.bigorange.game.Utils;
import org.bigorange.game.assets.MusicAsset;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.ui.TTFSkin;

public class MenuScreen extends BaseScreen {
    private static final String HIGHLIGHT_TEXT_DEACTIVATE = "[Deactivated]";
    // 菜单页面
    private final Table mainPage;
    // Credit页面， 后续还可以做设置页面
    private final Table creditsPage;
    private final TextButton continueItem;
    private final Slider volumeSlider;
    private final Array<TextButton> menuItems;
    private int currentitemIdx;
    private int volumeIdx;
    private int creditsIdx;
    private int changeVolume;
    private Table table;


    public MenuScreen(TTFSkin skin, ScreenManager screenManager){
        super(skin, screenManager);
        menuItems = new Array<>();
        final Stack menuPages = new Stack();
        volumeSlider = new Slider(0, 1, 0.01f, false, skin, "default-horizontal");
        //volumeSlider.setValue(initialVolumeValue);
        volumeSlider.setValue(0.5f);
        if (volumeSlider.getValue() == 0) {
            volumeSlider.setStyle(skin.get("deactivated", Slider.SliderStyle.class));
        }
        continueItem = new TextButton(HIGHLIGHT_TEXT_DEACTIVATE + "Continue", skin, "huge");
        mainPage = createMainPage();
        menuPages.add(mainPage);
        currentitemIdx = 0;
        volumeIdx = 1;
        creditsIdx = 2;
        highlightCurrentItem(true);

        creditsPage = createCreditsPage();
        menuPages.add(creditsPage);
        creditsPage.setVisible(false);

        table = new Table();
        table.setFillParent(true);
        table.add(menuPages).expand().fill();
        stage.addActor(table);
    }

    @Override
    public void show() {
        super.show();
        Utils.getAudioManager().playMusic(MusicAsset.INTRO);
    }

    @Override
    public void render(float delta) {
        //super.render(delta);

        if (changeVolume != 0) {
            if (changeVolume > 0) {
                moveSelectionRight();
            } else {
                moveSelectionLeft();
            }
            Utils.getAudioManager().setVolume(volumeSlider.getValue());
        }

        super.render(delta);

    }


    private Table createMainPage() {
        final Table content = new Table();
        //content.setBackground(skin.getDrawable("menu_background"));
       // content.add(new Image(skin.getDrawable("banner"))).expand().top().padTop(65).row();


        //menuItems.add(new TextButton(hud.getLocalizedString("newGame"), skin, "huge"));
        menuItems.add(new TextButton("New Game", skin, "huge"));
        content.add(menuItems.peek()).fill().expand().row();
        content.add(continueItem).fill().expand().row();

        final Table soundTable = new Table();
        //menuItems.add(new TextButton(hud.getLocalizedString("volume"), skin, "huge"));
        menuItems.add(new TextButton("Volume", skin, "huge"));
        soundTable.add(menuItems.peek()).fillX().expandX().row();
        soundTable.add(volumeSlider).expandX().width(250);
        content.add(soundTable).fill().expand().row();

        //menuItems.add(new TextButton(hud.getLocalizedString("creditsMenuItem"), skin, "huge"));
        menuItems.add(new TextButton("Credits Menu", skin, "huge"));
        content.add(menuItems.peek()).fill().expand().row();
        //menuItems.add(new TextButton(hud.getLocalizedString("quitGame"), skin, "huge"));
        menuItems.add(new TextButton("Quit Game", skin, "huge"));
        content.add(menuItems.peek()).fill().expand().padBottom(175).row();


        return content;
    }

    private Table createCreditsPage() {
        final Table content = new Table();
       // content.add(new Image(skin.getDrawable("banner"))).expandX().top().padTop(65).row();


        //final TextButton creditsTxt = new TextButton(hud.getLocalizedString("credits"), skin, "normal");
        final TextButton creditsTxt = new TextButton("Credits", skin, "normal");
        creditsTxt.getLabel().setWrap(true);
        //content.add(new TextButton(hud.getLocalizedString("creditsMenuItem")+":",skin, "huge")).expandX().top().padTop(75).row();
        content.add(new TextButton("Credits Menu Item"+":",skin, "huge")).expandX().top().padTop(75).row();
        content.add(creditsTxt).expandX().fill().top().pad(10, 25, 175, 25);

        return content;
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {
        switch (key){
            case UP -> {
                moveSelectionUp();
            }
            case DOWN -> {
                moveSelectionDown();
            }
            case LEFT -> {
                changeVolume = -1;
            }
            case RIGHT -> {
                changeVolume = 1;
            }
            case SELECT -> {
                if(isNewGameSelected()){
                    final UndergroundQuest gameInstance = Utils.getGameInstance();
                    screenManager.setScreenType(EScreenType.GAME);
                    return;
                }else if(isContinueSelected()){
                    return;
                }else if(isQuitGameSelected()){
                    Gdx.app.exit();
                    return;
                }
                selectCurrentItem();
            }

        }
    }

    @Override
    public void keyUP(InputManager manager, EKey key) {
        if(key == EKey.LEFT){
            changeVolume = manager.isKeyDown(EKey.RIGHT) ? 1: 0;
        } else if(key == EKey.RIGHT){
            changeVolume = manager.isKeyDown(EKey.LEFT) ? -1 : 0;
        }
    }
    public void moveSelectionRight(){
        if(currentitemIdx == volumeIdx){
            volumeSlider.setValue(volumeSlider.getValue() + volumeSlider.getStepSize());
            if(MathUtils.isEqual(volumeSlider.getValue() , 0.01f)){
                volumeSlider.setStyle(skin.get("default", Slider.SliderStyle.class));
            }
        }
    }

    public void moveSelectionLeft(){
        if(currentitemIdx == volumeIdx){
            volumeSlider.setValue(volumeSlider.getValue() - volumeSlider.getStepSize());
            if(volumeSlider.getValue() == 0){
                volumeSlider.setStyle(skin.get("deactivated", Slider.SliderStyle.class));
            }
        }
    }

    public void moveSelectionUp() {
        if (!mainPage.isVisible()) {
            return;
        }
        highlightCurrentItem(false);
        --currentitemIdx;
        if (currentitemIdx < 0) {
            currentitemIdx = menuItems.size - 1;
        }
        highlightCurrentItem(true);

    }

    public void moveSelectionDown(){
        if (!mainPage.isVisible()) {
            return;
        }
        highlightCurrentItem(false);
        ++currentitemIdx;
        if (currentitemIdx >= menuItems.size) {
            currentitemIdx = 0;
        }
        highlightCurrentItem(true);
    }
    public boolean isNewGameSelected(){
        return currentitemIdx == 0;
    }

    public boolean isQuitGameSelected() {
        return currentitemIdx == menuItems.size - 1;
    }

    public boolean isContinueSelected() {
        return currentitemIdx == 1;
    }

    public void selectCurrentItem(){
        if(currentitemIdx == creditsIdx && !creditsPage.isVisible()){
            mainPage.setVisible(false);
            creditsPage.setVisible(true);
        } else if(creditsPage.isVisible()){
            creditsPage.setVisible(false);
            mainPage.setVisible(true);
        }
    }

    private void highlightCurrentItem(final boolean highlight) {
        final Label label = menuItems.get(currentitemIdx).getLabel();
        if(highlight){
            label.getText().insert(0, "[Highlight]");
        } else {
            label.getText().replace("[Highlight]", "");
        }
        label.invalidateHierarchy();
    }

}
