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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.core.input.InputManager;
import org.bigorange.game.map.Map;
import org.bigorange.game.map.MapManager;

public class UndergroundQuest extends ApplicationAdapter {

	public static final String TAG = UndergroundQuest.class.getSimpleName();
	public static final float UNIT_SCALE = 1 / 48f;


	private OrthographicCamera camera;
	private Viewport viewport;
	private ResourceManager resourceManager;
	private MapManager mapManager;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Array<TiledMapTileLayer> layersToRender;
	private Vector2 direction;
	TiledMap tiledMap;
	SpriteBatch batch;
	Texture img;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(12.80f, 7.20f, camera);
		batch = new SpriteBatch();
		resourceManager = new ResourceManager();
		mapManager = new MapManager();

		resourceManager.load("map/battle1.tmx", TiledMap.class);
		resourceManager.finishLoading();
		tiledMap = resourceManager.get("map/battle1.tmx", TiledMap.class);
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, UNIT_SCALE, batch);
		layersToRender = tiledMap.getLayers().getByType(TiledMapTileLayer.class);
		img = new Texture("badlogic.jpg");


		direction = new Vector2();

		Gdx.input.setInputProcessor(new InputManager());
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		updateCamera();

		batch.begin();
		AnimatedTiledMapTile.updateAnimationBaseTime();
		mapRenderer.setView(camera);
		for (TiledMapTileLayer layer : layersToRender) {
			mapRenderer.renderTileLayer(layer);
		}
		batch.end();
	}
	private void updateCamera(){
		direction.set(0.0f, 0.0f);

		int mouseX = Gdx.input.getX();
		int mouseY = Gdx.input.getY();
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched() && mouseX < width * 0.25f)) {
			direction.x = -1;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isTouched() && mouseX > width * 0.75f)) {
			direction.x = 1;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.UP) || (Gdx.input.isTouched() && mouseY < height * 0.25f)) {
			direction.y = 1;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || (Gdx.input.isTouched() && mouseY > height * 0.75f)) {
			direction.y = -1;
		}

		direction.nor().scl(10.0f * Gdx.graphics.getDeltaTime());;

		camera.position.x += direction.x;
		camera.position.y += direction.y;

		TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get(0);

		float cameraMinX = viewport.getWorldWidth() * 0.5f;
		float cameraMinY = viewport.getWorldHeight() * 0.5f;
		float cameraMaxX = layer.getWidth() * layer.getTileWidth() * UNIT_SCALE - cameraMinX;
		float cameraMaxY = layer.getHeight() * layer.getTileHeight() * UNIT_SCALE - cameraMinY;

		camera.position.x = MathUtils.clamp(camera.position.x, cameraMinX, cameraMaxX);
		camera.position.y= MathUtils.clamp(camera.position.y, cameraMinY, cameraMaxY);

		camera.update();
	}


	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		resourceManager.dispose();
	}


	@Override
	public void resize(final int width, final int height) {
		viewport.update(width, height);
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public MapManager getMapManager() {
		return mapManager;
	}
}
