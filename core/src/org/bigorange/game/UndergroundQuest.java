package org.bigorange.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.bigorange.game.gamestate.EGameState;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.map.MapManager;

public class UndergroundQuest extends ApplicationAdapter {
	public static final String TAG = UndergroundQuest.class.getSimpleName();
	public static final float UNIT_SCALE = 1 / 64f;


	private OrthographicCamera camera;
	private ResourceManager resourceManager;
	private MapManager mapManager;
	private SpriteBatch spriteBatch;
	private Game game;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		spriteBatch = new SpriteBatch();
		resourceManager = new ResourceManager();
		mapManager = new MapManager();

		Gdx.input.setInputProcessor(new InputManager());
		this.game = new Game(EGameState.LOADING);
	}

	@Override
	public void render () {
		game.process();
	}

	@Override
	public void dispose () {
		spriteBatch.dispose();
		resourceManager.dispose();
	}


	@Override
	public void resize(final int width, final int height) {
		game.resize(width, height);
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public MapManager getMapManager() {
		return mapManager;
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public Game getGame() {
		return game;
	}
}
