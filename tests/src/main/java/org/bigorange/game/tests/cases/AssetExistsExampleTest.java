package org.bigorange.game.tests.cases;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.map.GameObject;
import org.bigorange.game.map.Map;
import org.bigorange.game.tests.framework.GdxTestRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class AssetExistsExampleTest {

    GdxTestRunner app;

    @Before
    public void startUp(){
        app = (GdxTestRunner)Gdx.app.getApplicationListener();
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

        ResourceManager resourceManager = app.getResourceManager();
        resourceManager.load("map/battle1.tmx", TiledMap.class);
        resourceManager.finishLoading();

        final TiledMap tiledMap = resourceManager.get("map/battle1.tmx", TiledMap.class);
        Map map = new Map(tiledMap);
        Assert.assertNotNull(map);
        Assert.assertNotNull(map.getGameObjects());
        for (GameObject gameObject : map.getGameObjects()) {
            System.err.println(gameObject.toString());
        }

    }
}
