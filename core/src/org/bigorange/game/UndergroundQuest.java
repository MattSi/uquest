package org.bigorange.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.ScreenUtils;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.map.Map;

public class UndergroundQuest extends ApplicationAdapter {

	public static final String TAG = UndergroundQuest.class.getSimpleName();
	public static final float UNIT_SCALE = 1 / 32f;


	private ResourceManager resourceManager;
	SpriteBatch batch;
	Texture img;

	@Override
	public void create () {
		resourceManager = new ResourceManager();
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");



	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		resourceManager.dispose();
	}


	@Override
	public void resize(final int width, final int height) {
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

}
