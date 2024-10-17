package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screen.MenuScreen;

public class MyGdxGame extends Game {

	public SpriteBatch batch;

	/**
	 * This method is called when the application is created.
	 * It initializes the SpriteBatch and the GameMap.
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MenuScreen(this));
	}

	/**
	 * This method is called when the application is disposed.
	 * It disposes the SpriteBatch to free up resources.
	 */
	@Override
	public void dispose() {
		batch.dispose();
		getScreen().dispose();
	}


}