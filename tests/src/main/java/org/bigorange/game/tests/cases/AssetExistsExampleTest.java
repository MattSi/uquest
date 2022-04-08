package org.bigorange.game.tests.cases;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.IntMap;
import org.bigorange.game.ResourceManager;
import org.bigorange.game.input.EKey;
import org.bigorange.game.map.MapGameObject;
import org.bigorange.game.map.Map;
import org.bigorange.game.map.MapManager;
import org.bigorange.game.tests.framework.GdxTestRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class AssetExistsExampleTest {

    GdxTestRunner app;
    ResourceManager resourceManager;

    @Before
    public void startUp(){
        app = (GdxTestRunner)Gdx.app.getApplicationListener();

        resourceManager = app.getResourceManager();
        resourceManager.load("map/battle1.tmx", TiledMap.class);
        resourceManager.finishLoading();
    }

    @After
    public void shutDown(){
        app.dispose();
    }

    @Test
    public void badlogicLogoFileExists(){

        Assert.assertTrue(Gdx.files.internal("badlogic.jpg").exists());
    }

    @Test
    public void gameMapExists(){

        final TiledMap tiledMap = resourceManager.get("map/battle1.tmx", TiledMap.class);
        Map map = new Map(tiledMap);
        Assert.assertNotNull(map);
        Assert.assertNotNull(map.getGameObjects());
        for (MapGameObject mapGameObject : map.getGameObjects()) {
            System.err.println(mapGameObject.toString());
        }
    }

    @Test
    public void gameAnimationTest(){
        final TiledMap tiledMap = resourceManager.get("map/battle1.tmx", TiledMap.class);
        final World world = new World(new Vector2(0,0),false);
        MapManager mapManager = app.getMapManager();
        mapManager.loadMap(tiledMap, world);
        final IntMap<Animation<Sprite>> gameObjectAnimationCache = mapManager.getGameObjectAnimationCache();
        Assert.assertNotNull(gameObjectAnimationCache);
    }

    @Test
    public void gameInputEKeyTest(){
        final EKey eKey = app.geteKey();
        System.err.println(eKey.ordinal());
    }
}
