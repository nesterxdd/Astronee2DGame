package com.mygdx.game.workbench;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Represents an arrow for workbench in the game.
 * This class extends the Actor class and provides functionality for drawing an arrow on the screen.
 *
 * @author Mykola Isaiev
 */
public class Arrow extends Actor {
    /**
     * The texture region of the arrow.
     */
    private TextureRegion textureRegion;

    /**
     * Constructor for creating an Arrow instance with a texture path and a position.
     *
     * @param path The path to the texture of the arrow.
     * @param position The position of the arrow.
     *
     * @author Mykola Isaiev
     */
    public Arrow(String path, Vector2 position) {
        Texture texture = new Texture(Gdx.files.internal(path));
        this.textureRegion = new TextureRegion(texture);

        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
        setPosition(position.x, position.y);

    }

    /**
     * Draws the arrow on the screen.
     * This method is overridden from the Actor class.
     *
     * @param batch The batch used for drawing.
     * @param parentAlpha The parent alpha value.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
