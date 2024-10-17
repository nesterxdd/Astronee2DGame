package com.mygdx.game.item.modules.storages;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.interfaces.Interactable;
import com.mygdx.game.item.modules.Module;
import com.mygdx.game.world.GameMap;

/**
 * Represents a storage module in the game.
 * This class extends the Module class and implements the Interactable interface.
 * It provides functionality for managing the inventory of the storage.
 *
 * @author Mykola Isaiev
 */
public abstract class Storage extends Module implements Interactable {
    /**
     * The inventory of the storage.
     */
    Inventory storageInventory;

    /**
     * Constructor for creating a Storage instance with a DragAndDrop instance.
     *
     * @param name The name of the storage.
     * @param weight The weight of the storage.
     * @param normalTexturePath The path to the normal texture of the storage.
     * @param size The size of the storage.
     * @param dragAndDrop The DragAndDrop instance associated with the storage.
     *
     * @author Mykola Isaiev
     */
    public Storage(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, normalTexturePath, size, dragAndDrop);
    }

    /**
     * Constructor for creating a Storage instance located in an inventory.
     *
     * @param name The name of the storage.
     * @param weight The weight of the storage.
     * @param normalTexturePath The path to the normal texture of the storage.
     * @param size The size of the storage.
     * @param inventory The inventory in which the storage is located.
     *
     * @author Mykola Isaiev
     */
    public Storage(String name, float weight, String normalTexturePath, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexturePath, size, inventory);
    }

    /**
     * Performs the actions of the storage in each game tick.
     * If the ESCAPE key is pressed and the storage inventory is visible, the inventory is hidden and the storage is removed from the active actors.
     *
     * @param delta The time in seconds since the last game tick.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && storageInventory.isVisible()) {
            storageInventory.setVisible(false);
            GameMap.getPlayer().getMap().removeActActor(this);
        }
    }

    /**
     * Interacts with the storage.
     * The storage inventory is made visible and the storage is added to the active actors.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void interact() {
        storageInventory.setVisible(true);
        GameMap.getPlayer().getMap().addActActor(this);
    }
}
