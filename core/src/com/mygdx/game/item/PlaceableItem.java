package com.mygdx.game.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TileType;

/**
 * Represents a placeable item in the game.
 * This class extends the Actor class and provides functionality for items that can be placed in the game world.
 *
 * @author Mykola Isaiev
 */
public class PlaceableItem extends Actor {
    /**
     * The texture of the item.
     */
    private TextureRegion textureRegion;

    /**
     * The item that is placeable.
     */
    private Item item;

    /**
     * Indicates whether the item is too far from the player to be placed.
     */
    boolean tooFar = false;

    /**
     * The last position of the item in the game world.
     */
    Vector2 lastPosition;

    /**
     * Constructor for creating a placeable item with a texture and an item.
     *
     * @param textureRegion The texture of the item.
     * @param item The item that is placeable.
     *
     * @author Mykola Isaiev
     */
    public PlaceableItem(TextureRegion textureRegion, Item item) {
        this.textureRegion = textureRegion;
        this.item = item;
        if (item.isWrapped()) {
            setSize(item.getSize().getLowerSize().getId() * TileType.TILE_SIZE, item.getSize().getLowerSize().getId() * TileType.TILE_SIZE);
        } else {
            setSize(item.getSize().getId() * TileType.TILE_SIZE, item.getSize().getId() *TileType.TILE_SIZE);
        }
    }

    /**
     * Constructor for creating a placeable item with a texture, an item, and a last position.
     *
     * @param textureRegion The texture of the item.
     * @param item The item that is placeable.
     * @param lastPosition The last position of the item in the game world.
     *
     * @author Mykola Isaiev
     */
    public PlaceableItem(TextureRegion textureRegion, Item item, Vector2 lastPosition) {
        this.textureRegion = textureRegion;
        this.item = item;
        if (item.isWrapped()) {
            setSize(item.getSize().getLowerSize().getId() * TileType.TILE_SIZE, item.getSize().getLowerSize().getId() * TileType.TILE_SIZE);
        } else {
            setSize(item.getSize().getId() * TileType.TILE_SIZE, item.getSize().getId() *TileType.TILE_SIZE);
        }
        this.lastPosition = lastPosition;
    }

    /**
     * Handles the actions of the item in each frame of the game.
     *
     * @param delta The time in seconds since the last frame.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void act(float delta) {
        Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        GameMap.getStage().getViewport().unproject(vec);
        float mouseX = vec.x;
        float mouseY = vec.y;
        setPosition(mouseX - (float) TileType.TILE_SIZE / 2, mouseY - (float) TileType.TILE_SIZE / 2);

        float distance = (float) Math.sqrt(Math.pow(mouseX - GameMap.getPlayer().getX(), 2) + Math.pow(mouseY - GameMap.getPlayer().getY(), 2));
        tooFar = distance > 100;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !tooFar) {
            if (GameMap.getPlayer().getMap().getTileTypeByLocation(1, vec.x, vec.y) == TileType.SKY) {
                GameMap.getPlayer().getMap().removeActActor(this);
                GameMap.getPlayer().getInventory().removeItem(item);
                GameMap.getPlayer().getMap().addActor(item);

                item.setWorldMode(new Vector2(vec.x, vec.y));
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (lastPosition != null) {
                GameMap.getPlayer().getMap().addActor(item);
                item.setPosition(lastPosition.x, lastPosition.y);
            }
            GameMap.getPlayer().getMap().removeActActor(this);
        }
    }

    /**
     * Draws the item on the screen.
     *
     * @param batch The batch used for drawing.
     * @param parentAlpha The parent's alpha, used for transparency.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (tooFar) {
            batch.setColor(Color.RED);
            batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            batch.setColor(new Color(1, 1, 1, 1));
        } else {
            batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }
}
