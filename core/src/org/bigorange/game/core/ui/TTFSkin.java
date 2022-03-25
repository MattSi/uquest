package org.bigorange.game.core.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

public class TTFSkin extends Skin {

    TTFSkin(final TextureAtlas atlas){
        super(atlas);

        Colors.put("Highlight", new Color(0xff7f50ff));
        Colors.put("Deactivated", new Color(0x88888877));
        Colors.put("Normal", new Color(0xffffffff));
        Colors.put("Black", new Color(0x000000ff));
    }

    @Override
    public void load(FileHandle skinFile) {
        super.load(skinFile);

        // enable markup for all fonts to use the Color of the constructor
        // when creating texts
        final ObjectMap<String, BitmapFont> allFonts = this.getAll(BitmapFont.class);
        if(allFonts == null){
            return;
        }
        for (final BitmapFont font : allFonts.values()) {
            font.getData().markupEnabled = true;
        }
    }

    @Override
    public void dispose() {
        ObjectMap<String, BitmapFont> fonts = this.getAll(BitmapFont.class);
        if(fonts != null){
            for (String bitmapFontKey : fonts.keys()) {
                remove(bitmapFontKey, BitmapFont.class);
            }
        }
        super.dispose();
    }
}
