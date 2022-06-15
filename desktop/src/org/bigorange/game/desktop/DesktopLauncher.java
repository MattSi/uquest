package org.bigorange.game.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.bigorange.game.UndergroundQuest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 720;
		config.width = 1280;
		//config.fullscreen = true;
		Application app = new LwjglApplication(new UndergroundQuest(), config);
		//new LwjglApplication(new OrthographicCameraExample());
		//app.setLogLevel(Application.LOG_DEBUG);
	}
}
