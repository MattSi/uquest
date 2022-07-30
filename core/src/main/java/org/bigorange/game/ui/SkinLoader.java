package org.bigorange.game.ui;


import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class SkinLoader extends AsynchronousAssetLoader<TTFSkin, SkinLoader.SkinParameter> {

    public SkinLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SkinParameter parameter) {
        if (parameter == null) {
            throw new GdxRuntimeException("Skin parameter can not be null.");
        }

        // texture atlas dependency
        final Array<AssetDescriptor> dependencies = new Array<>();
        dependencies.add(new AssetDescriptor(file.pathWithoutExtension() + ".atlas", TextureAtlas.class));

        for (int fontSize : parameter.fontSizeToCreate) {
            final FreetypeFontLoader.FreeTypeFontLoaderParameter fontParam = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
            fontParam.fontFileName = parameter.fontPath;

            // enable anti-aliasing
            fontParam.fontParameters.minFilter = Texture.TextureFilter.Linear;
            fontParam.fontParameters.magFilter = Texture.TextureFilter.Linear;

            // create font according to density of target device display
            fontParam.fontParameters.size = fontSize;
            fontParam.fontParameters.incremental = true; // 参数为true，可以显示中文

            dependencies.add(new AssetDescriptor("font" + fontSize + ".ttf", BitmapFont.class, fontParam));
        }

        return dependencies;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, SkinParameter parameter) {
        // do nothing, because TTFSkin is always loaded synchronously
    }

    @Override
    public TTFSkin loadSync(AssetManager manager, String fileName, FileHandle file, SkinParameter parameter) {
        // load atlas and create skin
        final String textureAtlasPath = file.pathWithoutExtension() + ".atlas";
        final TextureAtlas atlas = manager.get(textureAtlasPath, TextureAtlas.class);
        final TTFSkin skin = new TTFSkin(atlas);


        // add bitmap font to skin
        for (int fontSize : parameter.fontSizeToCreate) {
            skin.add("font_" + fontSize, manager.get("font" + fontSize + ".ttf"));
        }

        // load skin now because the fonts in the json file are now available
        skin.load(file);
        return skin;
    }


    public static class SkinParameter extends AssetLoaderParameters<TTFSkin> {
        private final String fontPath;
        private final int[] fontSizeToCreate;

        public SkinParameter(String fontPath, final int... fontSizeToCreate) {

            if(fontPath == null || fontPath.trim().isEmpty()){
                throw new GdxRuntimeException("font path cannot be null or empty.");
            }

            if(fontSizeToCreate.length == 0){
                throw new GdxRuntimeException("fontSizeToCreate has to contain at least one value.");
            }
            this.fontPath = fontPath;
            this.fontSizeToCreate = fontSizeToCreate;
        }
    }
}
