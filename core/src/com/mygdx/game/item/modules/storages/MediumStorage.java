package com.mygdx.game.item.modules.storages;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.InventorySlot;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.world.GameMap;

import java.util.ArrayList;

/**
 * Represents a medium storage module in the game.
 * This class extends the Storage class and provides functionality for managing the inventory of the medium storage.
 *
 * @author Mykola Isaiev
 */
public class MediumStorage extends Storage {
    /**
     * Constructor for creating a MediumStorage instance with a DragAndDrop instance.
     * This constructor also sets up the inventory slots of the medium storage.
     *
     * @param name The name of the medium storage.
     * @param weight The weight of the medium storage.
     * @param normalTexturePath The path to the normal texture of the medium storage.
     * @param size The size of the medium storage.
     * @param dragAndDrop The DragAndDrop instance associated with the medium storage.
     *
     * @author Mykola Isaiev
     */
    public MediumStorage(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, normalTexturePath, size, dragAndDrop);

        ArrayList<InventorySlot> inventorySlots = new ArrayList<>();

        inventorySlots.add(new InventorySlot(new Vector2(600, 325), ItemSize.SMALL, dragAndDrop));
        inventorySlots.add(new InventorySlot(new Vector2(720, 325), ItemSize.SMALL, dragAndDrop));

        inventorySlots.add(new InventorySlot(new Vector2(600, 425), ItemSize.SMALL, dragAndDrop));
        inventorySlots.add(new InventorySlot(new Vector2(720, 425), ItemSize.SMALL, dragAndDrop));

        storageInventory = new Inventory(dragAndDrop, inventorySlots);
        this.storageInventory.changePosition(50, 10);
        GameMap.getPlayer().getMap().addActor(storageInventory);
    }

    /**
     * Constructor for creating a MediumStorage instance located in an inventory.
     *
     * @param name The name of the medium storage.
     * @param weight The weight of the medium storage.
     * @param normalTexturePath The path to the normal texture of the medium storage.
     * @param size The size of the medium storage.
     * @param inventory The inventory in which the medium storage is located.
     *
     * @author Mykola Isaiev
     */
    public MediumStorage(String name, float weight, String normalTexturePath, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexturePath, size, inventory);
    }
}
