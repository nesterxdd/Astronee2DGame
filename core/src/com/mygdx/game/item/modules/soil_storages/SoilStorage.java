package com.mygdx.game.item.modules.soil_storages;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.Item;

/**
 * Represents a soil storage module in the game.
 * This abstract class extends the Item class and provides functionality for managing the soil storage.
 * It has a current amount of soil and a capacity which determines the maximum amount of soil it can hold.
 *
 * @author Yehor Nesterenko
 */
public abstract class SoilStorage extends Item {
    /**
     * The current amount of soil in the storage.
     */
    protected int currentAmount;

    /**
     * The maximum amount of soil the storage can hold.
     */
    protected final int capacity;

    /**
     * Constructor for creating a SoilStorage instance with a DragAndDrop instance.
     *
     * @param name The name of the soil storage.
     * @param weight The weight of the soil storage.
     * @param normalTexturePath The path to the normal texture of the soil storage.
     * @param size The size of the soil storage.
     * @param dragAndDrop The DragAndDrop instance associated with the soil storage.
     * @param currentAmount The current amount of soil in the storage.
     *
     * @author Yehor Nesterenko
     */
    public SoilStorage(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop, int currentAmount) {
        super(name, weight, normalTexturePath, size, dragAndDrop);
        this.currentAmount = currentAmount;
        this.capacity = setCapacity();
    }

    /**
     * Constructor for creating a SoilStorage instance located in an inventory.
     *
     * @param name The name of the soil storage.
     * @param weight The weight of the soil storage.
     * @param normalTexture The normal texture of the soil storage.
     * @param size The size of the soil storage.
     * @param inventory The inventory in which the soil storage is located.
     * @param currentAmount The current amount of soil in the storage.
     *
     * @author Yehor Nesterenko
     */
    public SoilStorage(String name, float weight, String normalTexture, ItemSize size, Inventory inventory, int currentAmount) {
        super(name, weight, normalTexture, size, inventory);
        this.currentAmount = currentAmount;
        this.capacity = setCapacity();
    }

    /**
     * Returns the current amount of soil in the storage.
     *
     * @return The current amount of soil in the storage.
     *
     * @author Yehor Nesterenko
     */
    public abstract int getCurrentAmount();

    /**
     * Sets the capacity of the soil storage.
     * This method is abstract and should be implemented in subclasses.
     *
     * @return The capacity of the soil storage.
     *
     * @author Yehor Nesterenko
     */
    protected abstract int setCapacity();

    /**
     * Returns the capacity of the soil storage.
     *
     * @return The capacity of the soil storage.
     *
     * @author Yehor Nesterenko
     */
    public abstract int getCapacity();

    /**
     * Sets the current amount of soil in the storage.
     * This method is abstract and should be implemented in subclasses.
     *
     * @param currentAmount The current amount of soil to set.
     *
     * @author Yehor Nesterenko
     */
    public abstract void setCurrentAmount(int currentAmount);
}
