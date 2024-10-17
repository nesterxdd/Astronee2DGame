package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
    * This class is responsible for drawing the drill radius around the player.
    *
    * @author Mykola Isaiev
 */
public class DrillRadius extends Actor {
    /** Flag to represent if drill radius is farther than 400.
     */
    private boolean tooFar = false;
    /** Texture of the drill radius.
     */
    private TextureRegion textureRegion;

    /** Default constructor for DrillRadius.
     */
    public DrillRadius() {
        Texture texture = new Texture(Gdx.files.internal("player/drillRadiusNormal.png"));
        textureRegion = new TextureRegion(texture);

        setSize(texture.getWidth(), texture.getHeight());
    }

    /** Method to check if the drill radius intersects with a rectangle.
     * @param coordinates The coordinates of the rectangle.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     * @return flag if the drill radius intersects with the rectangle.
     *
     * @author Mykola Isaiev
     */
    public boolean intersects(Vector2 coordinates, float width, float height) {
        float centerX = getX() + getWidth() / 2;
        float centerY = getY() + getHeight() / 2;

        // Add padding of 30 units to each side of the rectangle
        float rectangleX = coordinates.x - 30;
        float rectangleY = coordinates.y - 30;
        float rectangleWidth = width + 60;
        float rectangleHeight = height + 60;

        float rectangleCenterX = rectangleX + rectangleWidth / 2;
        float rectangleCenterY = rectangleY + rectangleHeight / 2;

        float dx = Math.abs(centerX - rectangleCenterX);
        float dy = Math.abs(centerY - rectangleCenterY);

        float combinedHalfWidths = getWidth() / 2 + rectangleWidth / 2;
        float combinedHalfHeights = getHeight() / 2 + rectangleHeight / 2;

        return dx <= combinedHalfWidths && dy <= combinedHalfHeights;
    }

    /** Method to set the flag if the drill radius is too far.
     * @param tooFar The flag if the drill radius is too far.
     *
     * @author Mykola Isaiev
     */
    public void setTooFar(boolean tooFar) {
        this.tooFar = tooFar;
    }

    /** Method to check if the drill radius is too far.
     * @return flag if the drill radius is too far.
     *
     * @author Mykola Isaiev
     */
    public boolean isTooFar() {
        return tooFar;
    }

    /** Method to draw the drill radius.
     * Using normal texture if the drill radius is not too far, red texture otherwise.
     *
     * @param batch The batch to draw the drill radius.
     * @param parentAlpha The alpha of the parent.
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