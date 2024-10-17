package com.mygdx.game.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TiledGameMap;

public class MainScreen implements Screen {
    private MyGdxGame game;
    private GameMap gameMap;

    SpriteBatch batch;
    private Stage pauseStage;
    private Skin skin;
    private Window pauseWindow;
    private boolean isPaused;

    public MainScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        gameMap = new TiledGameMap();
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);

        skin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        pauseStage = new Stage();
        createPauseWindow();
        isPaused = false;
    }

    private void createPauseWindow() {
        pauseWindow = new Window("Pause Menu", skin);
        float screenWidth = Gdx.graphics.getWidth()* 0.5f;
        float screenHeight = Gdx.graphics.getHeight()* 0.5f;

        pauseWindow.setSize(screenWidth, screenHeight);
        pauseWindow.setPosition((Gdx.graphics.getWidth() - pauseWindow.getWidth()) / 2,
                (Gdx.graphics.getHeight() - pauseWindow.getHeight()) / 2);



        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });

        TextButton quitButton = new TextButton("Exit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        TextButton backToMenuButton = new TextButton("Back to Menu", skin);
        backToMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setWindowedMode(755, 442);
                game.setScreen(new MenuScreen(game));
            }
        });

        pauseWindow.add(resumeButton).size(200, 50).pad(10);
        pauseWindow.row();
        pauseWindow.add(quitButton).size(200, 50).pad(10);
        pauseWindow.row();
        pauseWindow.add(backToMenuButton).size(200, 50).pad(10);
        pauseStage.addActor(pauseWindow);
        pauseWindow.setVisible(false);
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseWindow.setVisible(isPaused);
        if (isPaused) {
            Gdx.input.setInputProcessor(pauseStage);
        } else {
            Gdx.input.setInputProcessor(new InputMultiplexer(pauseStage, gameMap.getStage()));
        }
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            togglePause();
        }


        if (GameMap.cleared) {
            GameMap.cleared = false;
            Gdx.graphics.setWindowedMode(755, 442);
            game.setScreen(new MenuScreen(game));
        }

        if (!isPaused) {
            gameMap.update(Gdx.graphics.getDeltaTime());
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameMap.render(batch);

        if (isPaused) {
            pauseStage.act();
            pauseStage.draw();
        }



    }

    @Override
    public void resize(int width, int height) {
        gameMap.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }


    @Override
    public void dispose () {
        game.dispose();
        pauseStage.dispose();
        skin.dispose();
    }
}
