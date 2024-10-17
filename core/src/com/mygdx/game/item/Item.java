package com.mygdx.game.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.gui.FunctionInterface;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.InventorySlot;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.interfaces.Movable;
import com.mygdx.game.item.interfaces.Placeable;
import com.mygdx.game.item.interfaces.Refuelable;
import com.mygdx.game.item.interfaces.Wrapable;
import com.mygdx.game.item.modules.soil_storages.SmallSoilStorage;
import com.mygdx.game.item.resources.Resource;
import com.mygdx.game.workbench.Recipes;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TileType;

import java.util.HashMap;

/**
 * Represents an item in the game.
 * This abstract class provides a base for all items in the game, defining common properties and behaviors.
 *
 * @author Mykola Isaiev
 */
public abstract class Item extends Actor implements Movable, Placeable, Wrapable {
    /**
     * The current texture of the item.
     */
    protected TextureRegion currentTexture;

    /**
     * The normal texture of the item.
     */
    protected TextureRegion normalTexture;

    /**
     * The texture of the item when it is wrapped.
     */
    protected TextureRegion wrappedTexture;

    /**
     * The name of the item.
     */
    protected String name;

    /**
     * The weight of the item.
     */
    protected float weight;

    /**
     * The DragAndDrop instance associated with the item.
     */
    protected DragAndDrop dragAndDrop;

    /**
     * The inventory slot in which the item is located.
     */
    protected InventorySlot inventorySlot;

    /**
     * The size of the item.
     */
    protected ItemSize size;

    /**
     * Indicates whether the item is wrapped.
     */
    protected boolean wrapped = false;

    /**
     * Constructor for creating an item with a DragAndDrop instance.
     *
     * @param name The name of the item.
     * @param weight The weight of the item.
     * @param normalTexturePath The path to the normal texture of the item.
     * @param size The size of the item.
     * @param dragAndDrop The DragAndDrop instance associated with the item.
     *
     * @author Mykola Isaiev
     */
    public Item(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop) {
        this.name = name;
        this.weight = weight;

        Texture texture = new Texture(Gdx.files.internal(normalTexturePath));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.normalTexture = new TextureRegion(texture);

        this.currentTexture = normalTexture;

        texture = new Texture(Gdx.files.internal("items/wrapped/wrapped.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.wrappedTexture = new TextureRegion(texture);

        this.size = size;
        inventorySlot = null;

        setSize(size.getId() * TileType.TILE_SIZE * 4, size.getId() * TileType.TILE_SIZE * 4);

        this.dragAndDrop = dragAndDrop;
        initializeDragAndDrop();

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.RIGHT) {
                    if (getInventoryPosition() == null) {
                        if (!GameMap.getPlayer().isDrillMode()) {
                            float distance = (float) Math.sqrt(Math.pow(GameMap.getPlayer().getX() - getX(), 2) + Math.pow(GameMap.getPlayer().getY() - getY(), 2));
                            if (!GameMap.getPlayer().getInventory().isVisible() && !GameMap.getPlayer().getMap().getActionsPopUpList().isVisible() && distance < 100) {
                                handleWorldClick();
                            }
                        }
                    } else {
                        if (!GameMap.getPlayer().getMap().getActionsPopUpList().isVisible()) {
                            handleInventoryClick();
                        }
                    }
                }
                return true;
            }
        });
    }

    /**
     * Constructor for creating a craftable source item.
     *
     * @param name The name of the item.
     * @param weight The weight of the item.
     * @param normalTexturePath The path to the normal texture of the item.
     * @param size The size of the item.
     * @param inventory The inventory in which the item is located.
     *
     * @author Mykola Isaiev
     */
    public Item(String name, float weight, String normalTexturePath, ItemSize size, Inventory inventory) {
        this.name = name;
        this.weight = weight;

        Texture texture = new Texture(Gdx.files.internal(normalTexturePath));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.normalTexture = new TextureRegion(texture);
        this.currentTexture = normalTexture;

        this.wrappedTexture = new TextureRegion(new Texture(Gdx.files.internal("items/wrapped/wrapped.png")));

        this.size = size;
        inventorySlot = null;

        setSize(size.getId() * TileType.TILE_SIZE * 4, size.getId() * TileType.TILE_SIZE * 4);

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    if (!GameMap.getPlayer().getMap().getActionsPopUpList().isVisible()) {
                        Recipes.craft(name, inventory);
                    }
                }
                return true;
            }
        });
    }

    /**
     * Handles the click event when the item is in the inventory.
     *
     * @author Mykola Isaiev
     */
    public void handleInventoryClick() {
        HashMap<String, FunctionInterface> labels = new HashMap<>();

        labels.put("Place", () -> {
            this.place();
            this.getInventoryPosition().getInventory().setVisible(false);
            GameMap.getPlayer().getInventory().setVisible(false);
        });

        if (this instanceof Refuelable && !this.isWrapped()) {
            labels.put("Fuel", () -> {
                ((Refuelable) this).fuel();
            });
        }

        Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        GameMap.getStage().getViewport().unproject(vec);
        GameMap.getPlayer().getMap().getActionsPopUpList().addAction(labels, new Vector2(vec.x, vec.y));
    }

    /**
     * Handles the click event when the item is in the world.
     *
     * @author Mykola Isaiev
     */
    public void handleWorldClick() {
        HashMap<String, FunctionInterface> labels = new HashMap<>();

        if ((!wrapped && size == ItemSize.SMALL) || (wrapped && size.getLowerSize() == ItemSize.SMALL)) {
            labels.put("Pick up", () -> {
                this.pickUp();
            });
        }

        labels.put("Move", () -> {
            this.move();
        });

        if (!(this instanceof Resource || this instanceof SmallSoilStorage)) {
            if (!wrapped) {
                labels.put("Wrap", () -> {
                    this.wrap();
                });
            } else {
                labels.put("Unwrap", () -> {
                    this.unwrap();
                });
            }
        }

        Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        GameMap.getStage().getViewport().unproject(vec);
        GameMap.getPlayer().getMap().getActionsPopUpList().addAction(labels, new Vector2(vec.x, vec.y));
    }

    /**
     * Moves the item in the game world. The specific behavior is determined by the class that implements this method.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void move() {
        GameMap.getPlayer().getMap().removeActor(this);
        Vector2 lastPosition = new Vector2(getX(), getY());
        PlaceableItem rawResourcePlaceable = new PlaceableItem(currentTexture, this, lastPosition);
        GameMap.getPlayer().getMap().addActActor(rawResourcePlaceable);
    }

    /**
     * Places the item in the game world. The specific behavior is determined by the class that implements this method.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void place() {
        PlaceableItem rawResourcePlaceable = new PlaceableItem(currentTexture, this);
        GameMap.getPlayer().getMap().addActActor(rawResourcePlaceable);
    }

    /**
     * Wraps the item. This changes the item's size to its lower size and changes its texture to the wrapped texture.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void wrap() {
        wrapped = true;
        setSize(size.getLowerSize().getId() * TileType.TILE_SIZE, size.getLowerSize().getId() * TileType.TILE_SIZE);
        currentTexture = wrappedTexture;
    }

    /**
     * Unwraps the item. This changes the item's size to its original size and changes its texture to the normal texture.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void unwrap() {
        wrapped = false;
        setSize(size.getId() * TileType.TILE_SIZE, size.getId() * TileType.TILE_SIZE);
        currentTexture = normalTexture;
    }

    /**
     * Picks up the item and adds it to the player's inventory.
     *
     * @author Mykola Isaiev
     */
    public void pickUp() {
        GameMap.getPlayer().getMap().removeActor(this);
        setInventoryMode();
        GameMap.getPlayer().getInventory().addItem(this);
    }

    /**
     * Draws the item on the screen. The specific behavior is determined by the class that implements this method.
     *
     * @param batch The batch used for drawing.
     * @param parentAlpha The parent's alpha, used for transparency.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    /**
     * Sets the size of the item to fit within an inventory slot.
     *
     * @author Mykola Isaiev
     */
    public void setInventoryMode() {
        setSize(TileType.TILE_SIZE * 4, TileType.TILE_SIZE * 4);
    }

    /**
     * Sets the item's mode to world mode, which adjusts its size and position to fit within the game world.
     * If the item is wrapped, its size is set to its lower size. Otherwise, its size is set to its original size.
     *
     * @param position The position in the game world where the item should be placed.
     *
     * @author Mykola Isaiev
     */
    public void setWorldMode(Vector2 position) {
        inventorySlot = null;
        if (wrapped) {
            setSize(size.getLowerSize().getId() * TileType.TILE_SIZE, size.getLowerSize().getId() * TileType.TILE_SIZE);
        } else {
            setSize(size.getId() * TileType.TILE_SIZE, size.getId() * TileType.TILE_SIZE);
        }

        setPosition(position.x, position.y);
    }

    public void setRegion(TextureRegion region){
        currentTexture = region;
    }

    public void setInventoryPosition(InventorySlot slot) {
        this.inventorySlot = slot;
    }

    /**
     * Returns the inventory slot in which the item is located.
     *
     * @return The inventory slot in which the item is located.
     *
     * @author Mykola Isaiev
     */
    public InventorySlot getInventoryPosition() {
        return inventorySlot;
    }

    /**
     * Returns the size of the item.
     *
     * @return The size of the item.
     *
     * @author Mykola Isaiev
     */
    public ItemSize getSize() {
        return size;
    }

    /**
     * Returns whether the item is wrapped.
     *
     * @return True if the item is wrapped, false otherwise.
     *
     * @author Mykola Isaiev
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether the item is wrapped.
     *
     * @return True if the item is wrapped, false otherwise.
     *
     * @author Mykola Isaiev
     */
    public boolean isWrapped() {
        return wrapped;
    }

    /**
     * Initializes the DragAndDrop functionality for the item.
     *
     * @author Mykola Isaiev
     */
    private void initializeDragAndDrop() {
        dragAndDrop.addSource(new DragAndDrop.Source(this) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                if (!GameMap.getPlayer().getMap().getActionsPopUpList().isVisible() && inventorySlot != null) {
                    DragAndDrop.Payload payload = new DragAndDrop.Payload();
                    payload.setDragActor(Item.this);
                    dragAndDrop.setDragActorPosition(getActor().getWidth() / 2, -getActor().getHeight() / 2);

                    return payload;
                }
                return null;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if (target == null) {
                    if (inventorySlot != null) {
                        inventorySlot.add(Item.this);
                    }
                } else {
                    Actor actor = target.getActor();
                    if (actor instanceof InventorySlot) {
                        InventorySlot slot = (InventorySlot) actor;
                        ItemSize comparisonSize = wrapped ? size.getLowerSize() : size;
                        if (slot.getSize().compare(comparisonSize) < 0) {
                            inventorySlot.add(Item.this);
                        }
                    }
                }
            }
        });
    }


}
