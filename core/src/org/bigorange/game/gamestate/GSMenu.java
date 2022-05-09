package org.bigorange.game.gamestate;

import com.badlogic.gdx.Gdx;
import org.bigorange.game.core.gamestate.EGameState;
import org.bigorange.game.core.gamestate.GameState;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.ui.HUD;
import org.bigorange.game.ui.MenuUI;
import org.bigorange.game.ui.TTFSkin;
import org.bigorange.game.core.Utils;

public class GSMenu extends GameState<MenuUI> {
    private int changeVolume;

    public GSMenu(final EGameState type, final HUD hud){
        super(type, hud);
        changeVolume = 0;
    }

    @Override
    protected MenuUI createGameStateUI(HUD hud, TTFSkin skin) {
        return new MenuUI(hud, skin, 0);
    }

    @Override
    public void activate() {
        super.activate();
        //gameStateUI.

    }

    @Override
    public void step(float fixedTimeStep) {
        super.step(fixedTimeStep);
        if (changeVolume != 0) {
            if (changeVolume > 0) {
                gameStateUI.moveSelectionRight();
            } else {
                gameStateUI.moveSelectionLeft();
            }
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void keyDown(InputManager manager, EKey key) {
        switch (key){
            case UP -> {
                gameStateUI.moveSelectionUp();
            }
            case DOWN -> {
                gameStateUI.moveSelectionDown();
            }
            case LEFT -> {
                changeVolume = -1;
            }
            case RIGHT -> {
                changeVolume = 1;
            }
            case SELECT -> {
                if(gameStateUI.isNewGameSelected()){
                    Utils.setGameState(EGameState.GAME);
                    return;
                }else if(gameStateUI.isContinueSelected()){
                    Utils.setGameState(EGameState.GAME);
                    return;
                }else if(gameStateUI.isQuitGameSelected()){
                    Gdx.app.exit();
                    return;
                }
                gameStateUI.selectCurrentItem();
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
}
