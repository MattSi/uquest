package org.bigorange.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.bigorange.game.gamestate.EGameState;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.map.MapManager;

import java.security.PublicKey;

public class UndergroundQuest extends ApplicationAdapter {
	public static final String TAG = UndergroundQuest.class.getSimpleName();
	public static final float UNIT_SCALE = 1 / 64f;

	public static final short BIT_WORLD = 1<<0;
	public static final short BIT_PLAYER = 1<<1;
	public static final short BIT_GAME_OBJECT = 1<<2;



	private ResourceManager resourceManager;
	private MapManager mapManager;
	private SpriteBatch spriteBatch;
	private Game game;
	private WorldContactManager worldContactManager;


	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		resourceManager = new ResourceManager();
		mapManager = new MapManager();
		worldContactManager = new WorldContactManager();

		this.game = new Game(EGameState.LOADING);
		Gdx.app.debug(TAG, "Create=================================");
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

	public WorldContactManager getWorldContactManager() {
		return worldContactManager;
	}
}
