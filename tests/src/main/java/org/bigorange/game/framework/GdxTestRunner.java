package org.bigorange.game.framework;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;


/**
 * This class referenced https://github.com/TomGrill/gdx-testing
 */
public class GdxTestRunner extends BlockJUnit4ClassRunner implements ApplicationListener {

    private Map<FrameworkMethod, RunNotifier> invokeInRender = new HashMap<>();
    public GdxTestRunner(Class<?> klass) throws InitializationError{
        super(klass);
        HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();

        new HeadlessApplication(this, conf);
        Gdx.gl = Mockito.mock(GL20.class);
    }


    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        synchronized (invokeInRender){
            for (Map.Entry<FrameworkMethod, RunNotifier> item : invokeInRender.entrySet()) {
                super.runChild(item.getKey(), item.getValue());
            }
            invokeInRender.clear();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        synchronized (invokeInRender){
            invokeInRender.put(method, notifier);
        }

        // wait until that test was invoked

    }

    private void waitUntilInvokedInRenderMethod(){
        try{
            while (true){
                Thread.sleep(10);
                synchronized (invokeInRender){
                    if(invokeInRender.isEmpty())
                        break;
                }
            }
        }catch (InterruptedException e){
            // Ignored sleep Exception
        }
    }
}
