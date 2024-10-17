package com.mygdx.game.screen;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

public class MenuScreen extends ScreenAdapter {
    private Stage stage;
    private Viewport viewport;
    private Skin skin;
    private MyGdxGame game;
    private Texture backgroundTexture;

    public MenuScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show(){
        Gdx.graphics.setWindowedMode(755, 442);
        viewport = new FillViewport(755, 442);

        viewport.update(755, 442, true);

        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        backgroundTexture = new Texture(Gdx.files.internal("menu/BackMenu.jpg"));

        TextButton buttonPlay = addButton("Play");
        buttonPlay.setSize(200, 50); // Optional: Set a size for better visibility
        float x = (viewport.getWorldWidth() - buttonPlay.getWidth()) / 2;
        float y = (viewport.getWorldHeight() - buttonPlay.getHeight()) / 2;
        buttonPlay.setPosition(x, y);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width / 2, Gdx.graphics.getDisplayMode().height / 2);
                //viewport = new FillViewport(Gdx.graphics.getDisplayMode().width / 2, Gdx.graphics.getDisplayMode().height / 2);
                viewport = new FillViewport(1920, 1080);
                Gdx.graphics.setWindowedMode(1920, 1080);

                game.setScreen(new MainScreen(game));

            }
        });

        TextButton buttonExit = addButton("Exit");
        buttonExit.setSize(200, 50); // Optional: Set a size for better visibilit
        buttonExit.setPosition(x, y - 60);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(buttonPlay);
        stage.addActor(buttonExit);
        Gdx.input.setInputProcessor(stage);
    }

    private TextButton addButton(String name){
        TextButton button = new TextButton(name, skin);
        return button;
    }


    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(.1f, .1f, .15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        game.batch.begin();
        float x = (viewport.getWorldWidth() - backgroundTexture.getWidth()) / 2;
        float y = (viewport.getWorldHeight() - backgroundTexture.getHeight()) / 2;
        game.batch.draw(backgroundTexture, x, y);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }


    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);

    }
}