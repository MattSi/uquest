package org.bigorange.game.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.ResourceManager;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.system.PlayerAnimationSystem;
import org.bigorange.game.ecs.system.PlayerMovementSystem;
import org.bigorange.game.input.EKey;
import org.bigorange.game.input.InputManager;
import org.bigorange.game.map.Map;
import org.bigorange.game.map.MapManager;
import org.bigorange.game.ui.HUD;
import org.bigorange.game.ui.TTFSkin;
import org.bigorange.game.ui.GameUI;
import org.bigorange.game.utils.Utils;

public class GSGame extends GameState<GameUI>  {
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

        // TODO init entity component system

        this.ecsEngine = new ECSEngine(world, new OrthographicCamera());



        final TiledMap tiledMap = resourceManager.get("map/battle1.tmx", TiledMap.class);
        mapManager.loadMap(tiledMap, world);
        mapManager.spawnGameObjects(this.ecsEngine, this.ecsEngine.getGameObjEntities());

        final Map currentMap = mapManager.getCurrentMap();
        final Array<Vector2> playerStartLocations = currentMap.getPlayerStartLocations();
        this.ecsEngine.addPlayer(playerStartLocations.get(0));
        setCursor();
    }

    @Override
    protected GameUI createHUD(HUD hud, TTFSkin skin) {
        return new GameUI(skin);
    }

    @Override
    public void render(float alpha) {
        ecsEngine.render(alpha);
        super.render(alpha);
    }

    @Override
    public void activate() {
        super.activate();
        Utils.getInputManager().addKeyInputListener(ecsEngine.getSystem(PlayerMovementSystem.class));
        Utils.getInputManager().addMouseInputListener(ecsEngine.getSystem(PlayerMovementSystem.class));
        Utils.getInputManager().addMouseInputListener(ecsEngine.getSystem(PlayerAnimationSystem.class));
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
}
