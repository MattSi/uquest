package org.bigorange.game.ecs.system;

import org.bigorange.game.ecs.RenderSystem;
import org.bigorange.game.ui.PlayerHUD;

public class PlayerHUDRenderSystem implements RenderSystem {

    private PlayerHUD hud = null;
    public PlayerHUDRenderSystem(final PlayerHUD hud) {
        this.hud = hud;
    }

    @Override
    public void render(float alpha) {
        hud.render(alpha);
    }

    @Override
    public void resize(int width, int height) {
       hud.resize(width, height);
    }

    @Override
    public void dispose() {

    }
}
