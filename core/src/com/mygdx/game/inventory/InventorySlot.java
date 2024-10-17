package com.mygdx.game.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.item.Item;

/**
 * Represents an inventory slot in the game.
 * This class extends the Actor class from the libGDX library, which allows it to be drawn on the screen and interacted with.
 *
 * @author Mykola Isaiev
 */
public class InventorySlot extends Actor {

    /**
     * The texture that will be drawn as the inventory slot.
     */
    private TextureRegion texture;
    /**
     * The item that is currently in the inventory slot.
     */
    private Item item;
    /**
     * The DragAndDrop object that handles the dragging and dropping of items.
     */
    private DragAndDrop dragAndDrop;
    /**
     * The size of the inventory slot.
     */
    private ItemSize size;

    /**
     * Constructs a new InventorySlot object with a specified position, size, and DragAndDrop object.
     * The texture for the inventory slot is loaded from a file and a new TextureRegion is created from it.
     * The position and size of the inventory slot are set based on the parameters.
     *
     * @param position the position of the inventory slot
     * @param size the size of the inventory slot
     * @param dragAndDrop the DragAndDrop object that handles the dragging and dropping of items
     *
     * @author Mykola Isaiev
     */
    public InventorySlot(Vector2 position, ItemSize size, DragAndDrop dragAndDrop) {
        Texture texture = new Texture(Gdx.files.internal("inventory/inventorySlot.png"));
        this.texture = new TextureRegion(texture);

        this.size = size;

        setWidth(size.getWidth());
        setHeight(size.getHeight());
        setPosition(position.x, position.y);

        this.dragAndDrop = dragAndDrop;
        initializeDragAndDrop();
    }

    /**
     * Constructs a new InventorySlot object with a specified position and size.
     * The texture for the inventory slot is loaded from a file and a new TextureRegion is created from it.
     * The position and size of the inventory slot are set based on the parameters.
     *
     * @param position the position of the inventory slot
     * @param size the size of the inventory slot
     *
     * @author Mykola Isaiev
     */
    public InventorySlot(Vector2 position, ItemSize size) {
        Texture texture = new Texture(Gdx.files.internal("inventory/inventorySlot.png"));
        this.texture = new TextureRegion(texture);

        this.size = size;

        setWidth(size.getWidth());
        setHeight(size.getHeight());
        setPosition(position.x, position.y);
    }

    /**
     * Draws the inventory slot on the screen.
     *
     * @param batch the batch used to draw textures
     * @param parentAlpha the parent's alpha value, used to handle transparency
     *
     * @author Mykola Isaiev
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }


    /**
     * Adds an item to the inventory slot.
     * The item's position is set to the center of the inventory slot.
     *
     * @param item the item to add
     *
     * @author Mykola Isaiev
     */
    public void add(Item item) {
        this.item = item;
        item.setInventoryPosition(this);
        item.setPosition(getX() + getWidth() / 2 - item.getWidth() / 2, getY() + getHeight() / 2 - item.getHeight() / 2);
    }

    /**
     * Initializes the DragAndDrop object.
     * This method is called in the constructor that takes a DragAndDrop object as a parameter.
     *
     * @author Mykola Isaiev
     */
    private void initializeDragAndDrop() {
        dragAndDrop.addTarget(new DragAndDrop.Target(this) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                item = null;
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Actor actor = payload.getDragActor();
                if (actor instanceof Item) {
                    Item item = (Item) actor;
                    ItemSize comparisonSize = item.isWrapped() ? item.getSize().getLowerSize() : item.getSize();
                    if (size.compare(comparisonSize) >= 0) {
                        if (!getInventory().equals(item.getInventoryPosition().getInventory())) {
                            item.getInventoryPosition().getInventory().removeActor(item);
                            getInventory().addActor(item);
                        }
                        add(item);
                    }
                }
            }
        });
    }

    /**
     * Retrieves the item that is currently in the inventory slot.
     *
     * @return the item in the inventory slot
     *
     * @author Mykola Isaiev
     */
    public Item getItem() {
        return item;
    }

    /**
     * Removes the item from the inventory slot.
     *
     * @author Mykola Isaiev
     */
    public void removeItem() {
        item = null;
    }

    /**
     * Retrieves the size of the inventory slot.
     *
     * @return the size of the inventory slot
     *
     * @author Mykola Isaiev
     */
    public ItemSize getSize() {
        return size;
    }

    /**
     * Retrieves the inventory that the inventory slot is part of.
     *
     * @return the inventory that the inventory slot is part of
     *
     * @author Mykola Isaiev
     */
    public Inventory getInventory() {
        return (Inventory) getParent();
    }
}