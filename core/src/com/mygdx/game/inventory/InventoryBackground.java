package com.mygdx.game.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Represents the background of the inventory in the game.
 * This class extends the Actor class from the libGDX library, which allows it to be drawn on the screen and interacted with.
 *
 * @author Mykola Isaiev
 */
public class InventoryBackground extends Actor {

    /**
     * The texture that will be drawn as the background.
     */
    private TextureRegion textureRegion;

    /**
     * Default constructor for InventoryBackground.
     * It sets the texture of the background and its position on the screen.
     *
     * @author Mykola Isaiev
     */
    public InventoryBackground() {
        Texture texture = new Texture(Gdx.files.internal("inventory/inventoryBackground.png"));
        this.textureRegion = new TextureRegion(texture);
        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
        setPosition((float) Gdx.graphics.getWidth() / 2 - getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - getHeight() / 2);

    }

    /**
     * Method to draw the background on the screen.
     *
     * @param batch The batch to draw the background.
     * @param parentAlpha The alpha of the parent.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
