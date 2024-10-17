package com.mygdx.game.item.modules.crafting_tables;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.InventorySlot;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.world.GameMap;
import java.util.ArrayList;

/**
 * Represents a large workbench in the game.
 * This class extends the Workbench class and provides functionality for managing the crafting inventory of the large workbench.
 *
 * @author Mykola Isaiev
 */
public class LargeWorkbench extends Workbench {
    /**
     * Constructor for creating a LargeWorkbench instance with a DragAndDrop instance.
     * This constructor also sets up the inventory slots of the large workbench.
     *
     * @param name The name of the large workbench.
     * @param weight The weight of the large workbench.
     * @param normalTexturePath The path to the normal texture of the large workbench.
     * @param size The size of the large workbench.
     * @param dragAndDrop The DragAndDrop instance associated with the large workbench.
     *
     * @author Mykola Isaiev
     */
    public LargeWorkbench(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, normalTexturePath, size, dragAndDrop);

        ArrayList<InventorySlot> inventorySlots = new ArrayList<>();
        inventorySlots.add(new InventorySlot(new Vector2(600, 325), ItemSize.SMALL, dragAndDrop));
        inventorySlots.add(new InventorySlot(new Vector2(700, 325), ItemSize.SMALL, dragAndDrop));
        inventorySlots.add(new InventorySlot(new Vector2(600, 425), ItemSize.SMALL, dragAndDrop));
        inventorySlots.add(new InventorySlot(new Vector2(700, 425), ItemSize.SMALL, dragAndDrop));

        inventorySlots.add(new InventorySlot(new Vector2(605, 625), ItemSize.LARGE));

        InventorySlot craftingSlot = new InventorySlot(new Vector2(650, 525), ItemSize.SMALL, dragAndDrop);

        craftingInventory = new Inventory(dragAndDrop, inventorySlots);
        craftingInventory.addWorkbench(craftingSlot);

        this.craftingInventory.changePosition(200, 60);
        GameMap.getPlayer().getMap().addActor(craftingInventory);
    }

    /**
     * Constructor for creating a LargeWorkbench instance located in an inventory.
     *
     * @param name The name of the large workbench.
     * @param weight The weight of the large workbench.
     * @param normalTexturePath The path to the normal texture of the large workbench.
     * @param size The size of the large workbench.
     * @param inventory The inventory in which the large workbench is located.
     *
     * @author Mykola Isaiev
     */
    public LargeWorkbench(String name, float weight, String normalTexturePath, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexturePath, size, inventory);
    }
}
