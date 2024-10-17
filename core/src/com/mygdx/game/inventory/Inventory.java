package com.mygdx.game.inventory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.item.Item;
import com.mygdx.game.workbench.InventoryWorkbench;

import java.util.ArrayList;

/**
 * This class is responsible for creating an inventory for the player or module.
 * It contains a background and slots for items.
 * It also contains a workbench for crafting items.
 *
 * @author Mykola Isaiev
 * @author Yehor Nesterenko
 */

/**
 * This class is responsible for creating an inventory for the player or module.
 */
public class Inventory extends Group {
    /** Drag and drop object which applies to items and slots.
     */
    private DragAndDrop dragAndDrop;
    /** Background of the inventory.
     */
    private InventoryBackground inventoryBackground;
    /** List of inventory slots.
     */
    private ArrayList<InventorySlot> inventorySlots;
    /** Workbench for crafting items.
     */
    private InventoryWorkbench inventoryWorkbench;

    /**
     * Default constructor for Inventory.
     * @param dragAndDrop The drag and drop object.
     */
    public Inventory(DragAndDrop dragAndDrop) {
        this.inventoryBackground = new InventoryBackground();
        this.dragAndDrop = dragAndDrop;
        this.inventorySlots = new ArrayList<>();

        setup(100, 950);
    }

    /**
     * Constructor for Inventory with predefined slots.
     * @param dragAndDrop The drag and drop object.
     * @param inventorySlots The list of inventory slots.
     *
     * @author Mykola Isaiev
     */
    public Inventory(DragAndDrop dragAndDrop, ArrayList <InventorySlot> inventorySlots) {
        this.inventoryBackground = new InventoryBackground();
        this.dragAndDrop = dragAndDrop;
        this.inventorySlots = new ArrayList<>();
        this.inventorySlots.addAll(inventorySlots);

        predefinedSetup();
    }

    /**
     * Method to set up the inventory which was created with predefined inventory slots.
     *
     * @author Mykola Isaiev
     */
    private void predefinedSetup() {
        setVisible(false);
        inventoryBackground.setWidth(300);
        inventoryBackground.setHeight(575);
        addActor(inventoryBackground);

        for (InventorySlot inventorySlot : inventorySlots) {
            addActor(inventorySlot);
        }
    }

    /**
     * Method to set up the inventory.
     * @param x The x coordinate of the inventory.
     * @param y The y coordinate of the inventory.
     *
     * @author Mykola Isaiev
     */
    private void setup(int x, int y) {
        setVisible(false);
        inventoryBackground.setWidth(300);
        inventoryBackground.setHeight(575);
        inventoryBackground.setPosition(x, y);

        addActor(inventoryBackground);

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 2; j++) {
                InventorySlot inventorySlot = new InventorySlot(new Vector2((x-50) + 100 * j, (y+25) + 100 * i),
                        ItemSize.SMALL, dragAndDrop);

                inventorySlots.add(inventorySlot);
                addActor(inventorySlot);
            }
        }

        InventorySlot craftingSlot = new InventorySlot(new Vector2((x+100), (y+25)), ItemSize.SMALL, dragAndDrop);
        inventorySlots.add(craftingSlot);
        addActor(craftingSlot);

        this.inventoryWorkbench = new InventoryWorkbench(craftingSlot, dragAndDrop, this);
        addActor(inventoryWorkbench);
    }

    /**
     * Method to add a workbench to the inventory.
     * @param craftingSlot The crafting slot of the workbench.
     *
     * @author Mykola Isaiev
     */
    public void addWorkbench(InventorySlot craftingSlot) {
        inventorySlots.add(craftingSlot);
        addActor(craftingSlot);

        this.inventoryWorkbench = new InventoryWorkbench(craftingSlot, dragAndDrop, this);
        addActor(inventoryWorkbench);
    }

    /**
     * Method to remove an item from the inventory.
     * @param item Reference to item object to be removed.
     *
     * @author Mykola Isaiev
     */
    public void removeItem(Item item) {
        for (Actor actor : getChildren()) {
            if (actor == item) {
                item.getInventoryPosition().removeItem();
                item.setInventoryPosition(null);
                removeActor(actor);
                break;
            }
        }
    }

    /**
     * Method to remove an item from the inventory by its name.
     * @param name The name of the item to be removed.
     *
     * @author Mykola Isaiev
     */
    public void removeItem(String name) {
        for (Actor actor : getChildren()) {
            if (actor instanceof Item && ((Item) actor).getName().equals(name)) {
                ((Item) actor).getInventoryPosition().removeItem();
                ((Item) actor).setInventoryPosition(null);
                removeActor(actor);
                break;
            }
        }
    }

    /**
     * Method to add an item to the inventory.
     * @param item Reference to item object to be added.
     * @param inventorySlot The inventory slot where the item will be added.
     *
     * @author Mykola Isaiev
     */
    public void addItem(Item item, InventorySlot inventorySlot) {
        if (inventorySlot.getSize().compare(item.getSize()) >= 0) {
            inventorySlot.add(item);
            addActor(item);
        }
    }

    /**
     * Method to add an item to the inventory.
     * Choose the first available slot.
     * @param item Reference to item object to be added.
     *
     * @author Mykola Isaiev
     */
    public void addItem(Item item) {
        for (InventorySlot inventorySlot : inventorySlots) {
            ItemSize comparisonSize = item.isWrapped() ? item.getSize().getLowerSize() : item.getSize();

            if (inventorySlot.getItem() == null && inventorySlot.getSize().compare(comparisonSize) >= 0) {
                inventorySlot.add(item);
                addActor(item);
                return;
            }
        }
    }

    /**
     * Method to get size of the maximum slot in inventory.
     * @return size of the maximum slot in inventory
     *
     * @author Mykola Isaiev
     */
    public ItemSize hasMaxSize() {
        ItemSize maxSize = ItemSize.SMALL;
        for (InventorySlot inventorySlot : inventorySlots) {
            if (inventorySlot.getSize().compare(maxSize) > 0) {
                maxSize = inventorySlot.getSize();
            }
        }
        return maxSize;
    }

    /**
     * Changes the position of all actors in the inventory by a specified amount.
     *
     * @param x the amount to change in the x-coordinate
     * @param y the amount to change in the y-coordinate
     *
     * @author Mykola Isaiev
     */
    public void changePosition(float x, float y) {
        for (Actor actor : getChildren()) {
            actor.setPosition(actor.getX() + x, actor.getY() + y);
        }
    }

    /**
     * Changes the position of all actors in the inventory to a specified coordinate.
     * The change in position is calculated based on the difference between the new coordinates and the current position of the inventory background.
     *
     * @param x the new x-coordinate
     * @param y the new y-coordinate
     *
     * @author Mykola Isaiev
     */
    public void changePositionByCoordinates(float x, float y) {
        float xDiff = x - inventoryBackground.getX();
        float yDiff = y - inventoryBackground.getY();
        for (Actor actor : getChildren()) {
            actor.setPosition(actor.getX() + xDiff, actor.getY() + yDiff);
        }
    }

    /**
     * Retrieves the names of all items in the inventory.
     *
     * @return a list of item names
     *
     * @author Mykola Isaiev
     */
    public ArrayList<String> getItems() {
        ArrayList<String> items = new ArrayList<>();
        for (InventorySlot inventorySlot : inventorySlots) {

            if (inventorySlot.getItem() != null && inventorySlot != inventoryWorkbench.getInventorySlot()) {
                items.add(inventorySlot.getItem().getName());
            }
        }
        return items;
    }

    /**
     * Checks if an item with a specific name exists in the inventory.
     *
     * @param name the name of the item to check
     * @return true if the item exists, false otherwise
     *
     * @author Yehor Nesterenko
     */
    public boolean containsItem(String name) {
        for (InventorySlot inventorySlot : inventorySlots) {
            if (inventorySlot.getItem() != null && inventorySlot.getItem().getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves all items in the inventory that are instances of a specific class.
     *
     * @param clazz the class of the items to retrieve
     * @return a list of items that are instances of the specified class
     *
     * @author Yehor Nesterenko
     */
    public <T> ArrayList<T> getItemsOfClass(Class<T> clazz) {
        ArrayList<T> items = new ArrayList<>();
        for (InventorySlot inventorySlot : inventorySlots) {
            if (inventorySlot.getItem() != null && clazz.isInstance(inventorySlot.getItem()) && !inventorySlot.getItem().isWrapped()){
                items.add(clazz.cast(inventorySlot.getItem()));
            }
        }
        return items;
    }

    /**
     * Retrieves an item from the inventory that is an instance of a specific class and has a specific name.
     *
     * @param clazz the class of the item to retrieve
     * @param name the name of the item to retrieve
     * @return the item if it exists, null otherwise
     *
     * @author Yehor Nesterenko
     */
    public <T> T getItem(Class<T> clazz, String name) {
        ArrayList<T> items = new ArrayList<>();
        for (InventorySlot inventorySlot : inventorySlots) {
            if (inventorySlot.getItem() != null && clazz.isInstance(inventorySlot.getItem()) && inventorySlot.getItem().getName().equals(name)) {
               return clazz.cast(inventorySlot.getItem());
            }
        }
        return null;
    }
}