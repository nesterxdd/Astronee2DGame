package com.mygdx.game.item.modules.storages;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.InventorySlot;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.world.GameMap;
import java.util.ArrayList;

/**
 * Represents a big storage module in the game.
 * This class extends the Storage class and provides functionality for managing the inventory of the big storage.
 *
 * @author Mykola Isaiev
 */
public class BigStorage extends Storage {
    /**
     * Constructor for creating a BigStorage instance with a DragAndDrop instance.
     * This constructor also sets up the inventory slots of the big storage.
     *
     * @param name The name of the big storage.
     * @param weight The weight of the big storage.
     * @param normalTexturePath The path to the normal texture of the big storage.
     * @param size The size of the big storage.
     * @param dragAndDrop The DragAndDrop instance associated with the big storage.
     *
     * @author Mykola Isaiev
     */
    public BigStorage(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, normalTexturePath, size, dragAndDrop);

        ArrayList<InventorySlot> inventorySlots = new ArrayList<>();

        inventorySlots.add(new InventorySlot(new Vector2(600, 325), ItemSize.SMALL, dragAndDrop));
        inventorySlots.add(new InventorySlot(new Vector2(720, 325), ItemSize.SMALL, dragAndDrop));

        inventorySlots.add(new InventorySlot(new Vector2(600, 425), ItemSize.SMALL, dragAndDrop));
        inventorySlots.add(new InventorySlot(new Vector2(720, 425), ItemSize.SMALL, dragAndDrop));

        inventorySlots.add(new InventorySlot(new Vector2(600, 525), ItemSize.SMALL, dragAndDrop));
        inventorySlots.add(new InventorySlot(new Vector2(720, 525), ItemSize.SMALL, dragAndDrop));

        inventorySlots.add(new InventorySlot(new Vector2(600, 625), ItemSize.SMALL, dragAndDrop));
        inventorySlots.add(new InventorySlot(new Vector2(720, 625), ItemSize.SMALL, dragAndDrop));

        storageInventory = new Inventory(dragAndDrop, inventorySlots);
        this.storageInventory.changePosition(50, 10);
        GameMap.getPlayer().getMap().addActor(storageInventory);
    }

    /**
     * Constructor for creating a BigStorage instance located in an inventory.
     *
     * @param name The name of the big storage.
     * @param weight The weight of the big storage.
     * @param normalTexturePath The path to the normal texture of the big storage.
     * @param size The size of the big storage.
     * @param inventory The inventory in which the big storage is located.
     *
     * @author Mykola Isaiev
     */
    public BigStorage(String name, float weight, String normalTexturePath, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexturePath, size, inventory);
    }
}
