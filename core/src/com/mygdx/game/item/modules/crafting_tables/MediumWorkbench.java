package com.mygdx.game.item.modules.crafting_tables;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.InventorySlot;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.world.GameMap;

import java.util.ArrayList;

/**
 * Represents a medium workbench in the game.
 * This class extends the Workbench class and provides functionality for managing the crafting inventory of the medium workbench.
 *
 * @author Mykola Isaiev
 */
public class MediumWorkbench extends Workbench {
    /**
     * Constructor for creating a MediumWorkbench instance with a DragAndDrop instance.
     * This constructor also sets up the inventory slots of the medium workbench.
     *
     * @param name The name of the medium workbench.
     * @param weight The weight of the medium workbench.
     * @param normalTexturePath The path to the normal texture of the medium workbench.
     * @param size The size of the medium workbench.
     * @param dragAndDrop The DragAndDrop instance associated with the medium workbench.
     *
     * @author Mykola Isaiev
     */
    public MediumWorkbench(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, normalTexturePath, size, dragAndDrop);

        ArrayList<InventorySlot> inventorySlots = new ArrayList<>();
        inventorySlots.add(new InventorySlot(new Vector2(600, 325), ItemSize.SMALL, dragAndDrop));
        inventorySlots.add(new InventorySlot(new Vector2(700, 325), ItemSize.SMALL, dragAndDrop));

        inventorySlots.add(new InventorySlot(new Vector2(635, 525), ItemSize.MEDIUM));

        InventorySlot craftingSlot = new InventorySlot(new Vector2(650, 425), ItemSize.SMALL, dragAndDrop);

        craftingInventory = new Inventory(dragAndDrop, inventorySlots);
        craftingInventory.addWorkbench(craftingSlot);

        GameMap.getPlayer().getMap().addActor(craftingInventory);
    }

    /**
     * Constructor for creating a MediumWorkbench instance located in an inventory.
     *
     * @param name The name of the medium workbench.
     * @param weight The weight of the medium workbench.
     * @param normalTexturePath The path to the normal texture of the medium workbench.
     * @param size The size of the medium workbench.
     * @param inventory The inventory in which the medium workbench is located.
     *
     * @author Mykola Isaiev
     */
    public MediumWorkbench(String name, float weight, String normalTexturePath, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexturePath, size, inventory);
    }
}
