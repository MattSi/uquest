package org.bigorange.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.ResourceManager;
import org.bigorange.game.Utils;
import org.bigorange.game.assets.MapAsset;
import org.bigorange.game.assets.MusicAsset;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.system.PlayerContactSystem;
import org.bigorange.game.ecs.system.PlayerControlSystem;
import org.bigorange.game.gameobjs.GameObjectConfig;
import org.bigorange.game.gameobjs.GameObjectFactory;
import org.bigorange.game.map.Map;
import org.bigorange.game.map.MapManager;
import org.bigorange.game.message.MessageType;
import org.bigorange.game.ui.PlayerHUD;
import org.bigorange.game.ui.TTFSkin;

public class GameScreen extends BaseScreen implements PlayerContactSystem.PlayerContactListener, TelegramProvider,Telegraph {
    private final ECSEngine ecsEngine;
    private final World world;

    protected OrthographicCamera camera = null;
    protected OrthographicCamera hudCamera = null;
    private static final int VIEWPORT_WIDTH = 10;
    private static final int VIEWPORT_HEIGHT = 10;

    /**
     * 集成了不同的Viewport,
     * 屏幕，虚拟，物理
     * 比率
     */
    public static class VIEWPORT {
        public static float viewportWidth;
        public static float viewportHeight;
        public static float virtualWidth;
        public static float virtualHeight;
        public static float physicalWidth;
        public static float physicalHeight;
        public static float aspectRatio;
    }

    public GameScreen(TTFSkin skin, ScreenManager screenManager) {
        super(skin, screenManager);

        final MapManager mapManager = Utils.getMapManager();
        final ResourceManager resourceManager = Utils.getResourceManager();

        // TODO init box2d
        Box2D.init();
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(Utils.getWorldContactManager());
        world.setContinuousPhysics(true);


        // Camera setup
        setupViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, VIEWPORT.physicalWidth, VIEWPORT.physicalHeight);


        GameObjectFactory gof = GameObjectFactory.getInstance();
        gof.setWorldContactListener(Utils.getWorldContactManager());
        GameObjectConfig playConfig = gof.getGameObjectConfig(GameObjectFactory.GameObjectName.PLAYER);

        Gdx.app.log(TAG, playConfig.toString());

        // TODO, init Box2D light system

        ecsEngine = new ECSEngine(world, camera, hudCamera);
        ecsEngine.getSystem(PlayerContactSystem.class).addPlayerContactListener(this);

        final TiledMap tiledMap = resourceManager.get(MapAsset.LEVEL1.getFilePath(), TiledMap.class);
        mapManager.loadMap(tiledMap, world);
        mapManager.spawnGameObjects(this.ecsEngine, this.ecsEngine.getGameObjEntities());

        final Map currentMap = mapManager.getCurrentMap();
        final Array<Vector2> playerStartLocations = currentMap.getPlayerStartLocations();
        this.ecsEngine.addPlayer2(playerStartLocations.get(0));
        //setCursor();
        addEnemies();
        addNpcs();
        //addUI();
    }

    @Override
    public void show() {
        super.show();


        Utils.getInputManager().addKeyInputListener(ecsEngine.getSystem(PlayerControlSystem.class));
       // Utils.getInputManager().addMouseInputListener(ecsEngine.getSystem(PlayerAnimationSystem.class));
        Utils.getAudioManager().playMusic(MusicAsset.TALKING);
        Gdx.app.log(TAG, "Show......");

        final PlayerHUD playerHUD = (PlayerHUD) screenManager.getScreenInstance(EScreenType.PLAYERHUD);
        playerHUD.show();
    }

    @Override
    public void render(float delta) {
        ecsEngine.update(delta);
        world.step(delta, 6, 2);
        stage.act(delta);

        ecsEngine.render(delta);
        stage.getViewport().apply();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        setupViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        ecsEngine.resize(width, height);
    }

    @Override
    public void hide() {
        super.hide();

    }

    @Override
    public void wallContact() {

    }

    public void addEnemies() {
        MapManager mapManager = Utils.getMapManager();
        Map currentMap = mapManager.getCurrentMap();

        for (Vector2 location : currentMap.getEnemyStartLocations()) {
            ecsEngine.addEnemy(location, "Dog 01-3");
        }
    }

    public void addNpcs() {
        MapManager mapManager = Utils.getMapManager();
        Map currentMap = mapManager.getCurrentMap();

        for (Vector2 location : currentMap.getNpcStartLocations()) {
            //ecsEngine.addNpc(location, "Dog 01-1");
            ecsEngine.addNpc2(location,"", GameObjectFactory.GameObjectName.DOOR_KEEPER);
        }
    }

    private void setCursor() {
        final ResourceManager resourceManager = Utils.getResourceManager();
        final TextureAtlas.AtlasRegion atlasRegion = Utils.getResourceManager().get("characters/characters.atlas",
                TextureAtlas.class).findRegion("crosshair");

        final TextureData textureData = atlasRegion.getTexture().getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }
        Pixmap pixmap = new Pixmap(atlasRegion.getRegionWidth(), atlasRegion.getRegionHeight(), textureData.getFormat());
        pixmap.drawPixmap(
                textureData.consumePixmap(),
                0,
                0,
                atlasRegion.getRegionX(),
                atlasRegion.getRegionY(),
                atlasRegion.getRegionWidth(),
                atlasRegion.getRegionHeight()
        );
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pixmap, 0, 0));
        pixmap.dispose();
    }

    @Override
    public Object provideMessageInfo(int msg, Telegraph receiver) {
        return this;
    }



    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {
            case MessageType.MSG_NPC_TALK_TO_PLAYER -> {
            }
            case MessageType.PLAYER_AWAY_FROM_NPC -> {
               // showInfoMessage("", false);
            }
        }

        return true;
    }

    private void setupViewport(int width, int height) {
        //Make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;

        //Current viewport dimensions
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;

        //Pixel dimensions of display
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();

        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);

        //update viewport if there could be skewing
        if (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio) {
            //Letterbox left and right
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        } else {
            //Letterbox above and below
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight / VIEWPORT.physicalWidth);
        }

        Gdx.app.debug(TAG, "WorldRenderer: virtual: (" + VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")");
        Gdx.app.debug(TAG, "WorldRenderer: virtual: (" + VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")");
        Gdx.app.debug(TAG, "WorldRenderer: virtual: (" + VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")");
    }
}
