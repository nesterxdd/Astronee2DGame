package com.mygdx.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.List;
import java.util.Map;

/**
 * This class is responsible for creating a pop-up list of actions during interaction with items.
 *
 * @author Mykola Isaiev
 */
public class ActionsPopUpList extends Group {

    /**
     * Anonymous class is responsible for drawing the background of the pop-up list.
     *
     * @author Mykola Isaiev
     */
    private class ActionsPopUpBackground extends Actor {
        /** Texture of the background.
         */
        private TextureRegion textureRegion;

        /**
         * Default constructor for ActionsPopUpBackground.
         * @param position The position of the background.
         * @param width The width of the background.
         * @param height The height of the background.
         */
        public ActionsPopUpBackground(Vector2 position, int width, int height) {
            Texture texture = new Texture(Gdx.files.internal("actionPopUpBackground.png"));
            this.textureRegion = new TextureRegion(texture);
            setWidth(width);
            setHeight(height);
            setPosition(position.x, position.y - height);
        }

        /**
         * Method to draw the background.
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

    /** Background of the pop-up list.
     */
    private ActionsPopUpBackground actionsPopUpBackground;
    /** Style of the label.
     */
    Label.LabelStyle labelStyle;

    /**
     * Default constructor for ActionsPopUpList.
     */
    public ActionsPopUpList() {
        labelStyle = new Label.LabelStyle();
        BitmapFont myFont = new BitmapFont();
        labelStyle.font = myFont;
        labelStyle.font.getData().setScale(2);
        labelStyle.fontColor = Color.WHITE;
    }

    /**
     * Method to add actions to the pop-up list.
     * @param labels The labels of the actions.
     * @param position The position of the pop-up list.
     *
     * @author Mykola Isaiev
     */
    public void addAction(Map<String, FunctionInterface> labels, Vector2 position) {
        actionsPopUpBackground = new ActionsPopUpBackground(new Vector2(position.x + 25, position.y), 250, 50 * labels.size());
        addActor(actionsPopUpBackground);

        int i = 0;
        for (Map.Entry<String, FunctionInterface> entry : labels.entrySet()) {
            i++;
            Label label = new Label(entry.getKey(), labelStyle);
            label.setSize(250, 50);
            label.setPosition(position.x + 35, position.y - 50 * i);

            label.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    entry.getValue().execute();
                    dispose();
                }
            });

            addActor(label);
        }
    }

    /**
     * Method to dispose the pop-up list.
     * It removes all children from the pop-up list.
     *
     * @author Mykola Isaiev
     */
    public void dispose() {
        if (getChildren().size > 0) {
            getChildren().removeRange(0, getChildren().size - 1);
        }
    }

    /**
     * Method to check if the pop-up list is visible.
     * @return flag if the pop-up list is visible.
     *
     * @author Mykola Isaiev
     */
    public boolean isVisible() {
        return getChildren().size > 0;
    }
}
