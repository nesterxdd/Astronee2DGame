package com.mygdx.game;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * This is the launcher class for the desktop version of the game.
 * It sets up the configuration for the game window and starts the game.
 *
 * @author Mykola Isaiev
 */
public class DesktopLauncher {
	/**
	 * The main method that serves as the entry point for the application.
	 * It sets up the configuration for the game window and starts the game.
	 *
	 * @param arg The command line arguments.
	 */
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Astro Quest");
		config.setWindowedMode(1920, 1080);
		new Lwjgl3Application(new MyGdxGame(), config);
	}
}



