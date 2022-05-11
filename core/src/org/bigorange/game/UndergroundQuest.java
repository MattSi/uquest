package org.bigorange.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.DefaultTimepiece;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.bigorange.game.core.AudioManager;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.core.gamestate.EGameState;
import org.bigorange.game.map.MapManager;

public class UndergroundQuest extends ApplicationAdapter {
	public static final String TAG = UndergroundQuest.class.getSimpleName();
	public static final float UNIT_SCALE = 1 / 64f;


	public static final short CATEGORY_PLAYER = 1<<0; // 玩家

	public static final short CATEGORY_ENEMY = 1<<1; // 敌人

	public static final short CATEGORY_BULLET = 1<<2; //子弹
	public static final short CATEGORY_TILEMAP_OBJECT = 1<<3; // TileMap生成的对象

	public static final short CATEGORY_SENSOR = 1<<9;// SENSOR
	public static final short CATEGORY_WORLD = 1<<10; // 碰撞区域, 碰撞多边形, 世界


	public static final short MASK_PLAYER = ~CATEGORY_PLAYER; // 不能和Player碰撞
	public static final short MASK_TILEMAP_OBJECT = ~CATEGORY_TILEMAP_OBJECT; //不能和TILEMAP OBJECT碰撞
	public static final short MASK_ENEMY = ~CATEGORY_ENEMY; //不能和ENEMY 碰撞
	public static final short MASK_SENSOR = CATEGORY_PLAYER; // 只能和PLAYER碰撞
	public static final short MASK_BULLET = ~CATEGORY_BULLET;
	public static final short MASK_GROUND = -1;// 可以和任何物体碰撞

	private ResourceManager resourceManager;
	private MapManager mapManager;
	private SpriteBatch spriteBatch;
	private Game game;
	private WorldContactManager worldContactManager;
	private AudioManager audioManager;


	@Override
	public void create () {
		MessageManager.getInstance().clear();
		MessageManager.getInstance().setDebugEnabled(true);
		GdxAI.setTimepiece(new DefaultTimepiece(0.2f));
		spriteBatch = new SpriteBatch();
		resourceManager = new ResourceManager();
		mapManager = new MapManager();
		worldContactManager = new WorldContactManager();
		audioManager = new AudioManager();

		this.game = new Game(EGameState.LOADING);
		Gdx.app.debug(TAG, "Create=================================");
	}

	@Override
	public void render () {
		game.process();
		GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());
		MessageManager.getInstance().update();
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

	public AudioManager getAudioManager() {
		return audioManager;
	}
}
