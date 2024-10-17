package com.mygdx.game.item.modules.soil_storages;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;

/**
 * Represents a small soil storage module in the game.
 * This class extends the SoilStorage class and provides functionality for managing the soil storage.
 * It has a current amount of soil and a capacity which determines the maximum amount of soil it can hold.
 *
 * @author Yehor Nesterenko
 */
public class SmallSoilStorage extends SoilStorage{
    /**
     * The current amount of soil in the storage.
     */
    protected int currentAmount;

    /**
     * The maximum amount of soil the storage can hold.
     */
    protected final int capacity;

    /**
     * Constructor for creating a SmallSoilStorage instance with a DragAndDrop instance.
     *
     * @param name The name of the small soil storage.
     * @param weight The weight of the small soil storage.
     * @param normalTexturePath The path to the normal texture of the small soil storage.
     * @param size The size of the small soil storage.
     * @param dragAndDrop The DragAndDrop instance associated with the small soil storage.
     * @param currentAmount The current amount of soil in the storage.
     *
     * @author Yehor Nesterenko
     */
    public SmallSoilStorage(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop, int currentAmount) {
        super(name, weight, normalTexturePath, size, dragAndDrop, currentAmount);
        capacity = setCapacity();
    }

    /**
     * Constructor for creating a SmallSoilStorage instance located in an inventory.
     *
     * @param name The name of the small soil storage.
     * @param weight The weight of the small soil storage.
     * @param normalTexture The normal texture of the small soil storage.
     * @param size The size of the small soil storage.
     * @param inventory The inventory in which the small soil storage is located.
     * @param currentAmount The current amount of soil in the storage.
     *
     * @author Yehor Nesterenko
     */
    public SmallSoilStorage(String name, float weight, String normalTexture, ItemSize size, Inventory inventory, int currentAmount) {
        super(name, weight, normalTexture, size, inventory, currentAmount);
        capacity = setCapacity();
    }

    /**
     * Returns the current amount of soil in the storage.
     *
     * @return The current amount of soil in the storage.
     *
     * @author Yehor Nesterenko
     */
    @Override
    public int getCurrentAmount() {
        return currentAmount;
    }

    /**
     * Sets the current amount of soil in the storage.
     * If the current amount is greater than the capacity, it sets the current amount to the capacity.
     * If the current amount is less than zero, it sets the current amount to zero.
     *
     * @param currentAmount The current amount of soil to set.
     *
     * @author Yehor Nesterenko
     */
    @Override
    public void setCurrentAmount(int currentAmount) {
        if (currentAmount > capacity) {
            this.currentAmount = capacity;
        } else this.currentAmount = Math.max(currentAmount, 0);
    }

    /**
     * Sets the capacity of the small soil storage.
     * The capacity of the small soil storage is set to 100.
     *
     * @return The capacity of the small soil storage.
     *
     * @author Yehor Nesterenko
     */
    @Override
    protected int setCapacity() {
        return 100;
    }

    /**
     * Returns the capacity of the small soil storage.
     *
     * @return The capacity of the small soil storage.
     *
     * @author Yehor Nesterenko
     */
    @Override
    public int getCapacity() {
        return capacity;
    }

}
