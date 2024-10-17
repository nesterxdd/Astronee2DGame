package com.mygdx.game.item.modules.oxygen;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.interfaces.Refuelable;
import com.mygdx.game.item.modules.Module;
import com.mygdx.game.item.resources.RawResource;
import com.mygdx.game.world.GameMap;

/**
 * Represents an oxygen module in the game.
 * This abstract class extends the Module class and implements the Refuelable interface.
 * It provides the basic functionality for managing the fuel of an oxygen module.
 *
 * @author Mykola Isaiev
 */
public abstract class OxygenModule extends Module implements Refuelable {
    /**
     * The maximum amount of fuel the oxygen module can hold.
     */
    protected float fuelCapacity;

    /**
     * The current amount of fuel in the oxygen module.
     */
    protected float currentFuel;

    /**
     * Constructor for creating an OxygenModule instance with a DragAndDrop instance.
     *
     * @param name The name of the oxygen module.
     * @param weight The weight of the oxygen module.
     * @param normalTexturePath The path to the normal texture of the oxygen module.
     * @param size The size of the oxygen module.
     * @param dragAndDrop The DragAndDrop instance associated with the oxygen module.
     *
     * @author Mykola Isaiev
     */
    public OxygenModule(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, normalTexturePath, size, dragAndDrop);
    }

    /**
     * Constructor for creating an OxygenModule instance located in an inventory.
     *
     * @param name The name of the oxygen module.
     * @param weight The weight of the oxygen module.
     * @param normalTexturePath The path to the normal texture of the oxygen module.
     * @param size The size of the oxygen module.
     * @param inventory The inventory in which the oxygen module is located.
     *
     * @author Mykola Isaiev
     */
    public OxygenModule(String name, float weight, String normalTexturePath, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexturePath, size, inventory);
    }

    /**
     * Sets the fuel capacity of the oxygen module.
     *
     * @param fuelCapacity The fuel capacity to set.
     *
     * @author Mykola Isaiev
     */
    protected void setFuelCapacity(float fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    /**
     * Sets the current fuel of the oxygen module.
     *
     * @param currentFuel The current fuel to set.
     *
     * @author Mykola Isaiev
     */
    protected void setCurrentFuel(float currentFuel) {
        this.currentFuel = currentFuel;
    }

    /**
     * Adds fuel to the oxygen module.
     *
     * @param fuelAmount The amount of fuel to add.
     *
     * @author Mykola Isaiev
     */
    public void refuel(float fuelAmount) {
        if(currentFuel + fuelAmount > fuelCapacity){
            currentFuel = fuelCapacity;
            return;
        }
        currentFuel += fuelAmount;
    }

    /**
     * Uses fuel from the oxygen module.
     *
     * @param fuelAmount The amount of fuel to use.
     *
     * @author Mykola Isaiev
     */
    public void useFuel(float fuelAmount) {
        currentFuel -= fuelAmount;
    }

    /**
     * Checks if the oxygen module has fuel.
     *
     * @return True if the oxygen module has fuel, false otherwise.
     *
     * @author Mykola Isaiev
     */
    public boolean hasFuel() {
        return currentFuel > 0;
    }

    /**
     * Fuels the oxygen module using coal from the player's inventory.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void fuel(){
        RawResource coal;
        if ((coal = GameMap.getPlayer().getInventory().getItem(RawResource.class, "Coal")) != null) {
            this.refuel(1000);
            GameMap.getPlayer().getInventory().removeItem(coal);
        }
    }
}
