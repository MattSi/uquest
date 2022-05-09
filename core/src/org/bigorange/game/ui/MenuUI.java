package org.bigorange.game.ui;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.core.ui.BaseUI;

public class MenuUI extends BaseUI {

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

    public MenuUI(final HUD hud, final TTFSkin skin, final float initialVolumeValue) {
        super(skin);

        menuItems = new Array<>();
        final Stack menuPages = new Stack();
        volumeSlider = new Slider(0, 1, 0.01f, false, skin, "default");
        volumeSlider.setValue(initialVolumeValue);
        if (volumeSlider.getValue() == 0) {
            volumeSlider.setStyle(skin.get("deactivated", Slider.SliderStyle.class));
        }
        continueItem = new TextButton(HIGHLIGHT_TEXT_DEACTIVATE + hud.getLocalizedString("continue"), skin, "huge");
        mainPage = createMainPage(hud, skin);
        menuPages.add(mainPage);
        currentitemIdx = 0;
        volumeIdx = 1;
        creditsIdx = 2;
        highlightCurrentItem(true);

        creditsPage = createCreditsPage(hud, skin);
        menuPages.add(creditsPage);
        creditsPage.setVisible(false);

        add(menuPages).expand().fill();
        //debugAll();
    }

    private Table createCreditsPage(HUD hud, TTFSkin skin) {
        final Table content = new Table();
        content.add(new Image(skin.getDrawable("banner"))).expandX().top().padTop(65).row();


        final TextButton creditsTxt = new TextButton(hud.getLocalizedString("credits"), skin, "normal");
        creditsTxt.getLabel().setWrap(true);
        content.add(new TextButton(hud.getLocalizedString("creditsMenuItem")+":",skin, "huge")).expandX().top().padTop(75).row();
        content.add(creditsTxt).expandX().fill().top().pad(10, 25, 175, 25);

        return content;
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

    private Table createMainPage(final HUD hud, final TTFSkin skin) {
        final Table content = new Table();
        //content.setBackground(skin.getDrawable("menu_background"));
        content.add(new Image(skin.getDrawable("banner"))).expand().top().padTop(65).row();


        menuItems.add(new TextButton(hud.getLocalizedString("newGame"), skin, "huge"));
        content.add(menuItems.peek()).fill().expand().row();
        content.add(continueItem).fill().expand().row();

        final Table soundTable = new Table();
        menuItems.add(new TextButton(hud.getLocalizedString("volume"), skin, "huge"));
        soundTable.add(menuItems.peek()).fillX().expandX().row();
        soundTable.add(volumeSlider).expandX().width(250);
        content.add(soundTable).fill().expand().row();

        menuItems.add(new TextButton(hud.getLocalizedString("creditsMenuItem"), skin, "huge"));
        content.add(menuItems.peek()).fill().expand().row();
        menuItems.add(new TextButton(hud.getLocalizedString("quitGame"), skin, "huge"));
        content.add(menuItems.peek()).fill().expand().padBottom(175).row();

        return content;
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


}
