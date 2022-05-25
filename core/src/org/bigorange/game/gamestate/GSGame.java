package org.bigorange.game.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.core.assets.MapAsset;
import org.bigorange.game.core.assets.MusicAsset;
import org.bigorange.game.core.gamestate.EGameState;
import org.bigorange.game.core.gamestate.GameState;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.system.PlayerAnimationSystem;
import org.bigorange.game.ecs.system.PlayerContactSystem;
import org.bigorange.game.ecs.system.PlayerControlSystem;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.map.Map;
import org.bigorange.game.map.MapManager;
import org.bigorange.game.ui.HUD;
import org.bigorange.game.ui.TTFSkin;
import org.bigorange.game.ui.GameUI;
import org.bigorange.game.core.Utils;

public class GSGame extends GameState<GameUI> implements PlayerContactSystem.PlayerContactListener {
    private final ECSEngine ecsEngine;
    private final World world;

    public GSGame(final EGameState type, final HUD hud) {
        super(type, hud);

        final MapManager mapManager = Utils.getMapManager();
        final ResourceManager resourceManager = Utils.getResourceManager();
        // TODO init box2d
        Box2D.init();
        world = new World(new Vector2(0,0), true);
        world.setContactListener(Utils.getWorldContactManager());
        world.setContinuousPhysics(true);

        // TODO init player light

        this.ecsEngine = new ECSEngine(world, new OrthographicCamera());
        this.ecsEngine.getSystem(PlayerContactSystem.class).addPlayerContactListener(this);


        final TiledMap tiledMap = resourceManager.get(MapAsset.LEVEL1.getFilePath(), TiledMap.class);
        mapManager.loadMap(tiledMap, world);
        mapManager.spawnGameObjects(this.ecsEngine, this.ecsEngine.getGameObjEntities());

        final Map currentMap = mapManager.getCurrentMap();
        final Array<Vector2> playerStartLocations = currentMap.getPlayerStartLocations();
        this.ecsEngine.addPlayer(playerStartLocations.get(0));
        //setCursor();
        addEnemies();
        addNpcs();


    }

    @Override
    protected GameUI createGameStateUI(HUD hud, TTFSkin skin) {
        return new GameUI(hud, skin);
    }

    @Override
    public void render(float alpha) {
        ecsEngine.render(alpha);
        super.render(alpha);
    }

    @Override
    public void activate() {
        super.activate();
        Utils.getInputManager().addKeyInputListener(ecsEngine.getSystem(PlayerControlSystem.class));
        Utils.getInputManager().addMouseInputListener(ecsEngine.getSystem(PlayerControlSystem.class));
        Utils.getInputManager().addMouseInputListener(ecsEngine.getSystem(PlayerAnimationSystem.class));
        Utils.getAudioManager().playMusic(MusicAsset.TALKING);
    }

    @Override
    public void dispose() {
        ecsEngine.dispose();
    }

    @Override
    public void resize(int width, int height) {
        ecsEngine.resize(width, height);
        super.resize(width, height);
    }

    @Override
    public void step(float fixedTimeStep) {
        ecsEngine.update(fixedTimeStep);
        world.step(fixedTimeStep, 6, 2);
        super.step(fixedTimeStep);
    }

    @Override
    public void keyDown(InputManager manager, EKey key) {

    }

    @Override
    public void keyUP(InputManager manager, EKey key) {

    }

    private void setCursor(){
        final ResourceManager resourceManager = Utils.getResourceManager();
        final TextureAtlas.AtlasRegion atlasRegion = Utils.getResourceManager().get("characters/characters.atlas",
                TextureAtlas.class).findRegion("crosshair");

        final TextureData textureData = atlasRegion.getTexture().getTextureData();
        if(!textureData.isPrepared()){
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
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pixmap, 0,0));
        pixmap.dispose();
    }

    @Override
    public void wallContact() {
        /**
         * 1. 暂停游戏
         * 2. 显示对话框
         * 3. 按住空格，对话框消失，恢复游戏
         */
        //gameStateUI.showInfoMessage("Fuck", 2.0f);
    }

    public void addEnemies(){
        MapManager mapManager = Utils.getMapManager();
        Map currentMap = mapManager.getCurrentMap();

        for (Vector2 location : currentMap.getEnemyStartLocations()) {
            ecsEngine.addEnemy(location, "Enemy 09-1");
        }
    }

    public void addNpcs(){
        MapManager mapManager = Utils.getMapManager();
        Map currentMap = mapManager.getCurrentMap();

        for (Vector2 location : currentMap.getNpcStartLocations()) {
            ecsEngine.addNpc(location, "Enemy 06-1");
        }
    }
}
