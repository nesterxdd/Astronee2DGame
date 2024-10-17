package com.mygdx.game.item.modules.utils;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.interfaces.Refuelable;
import com.mygdx.game.item.resources.RawResource;
import com.mygdx.game.world.GameMap;

/**
 * Represents a jetpack in the game.
 * This class extends the Item class and implements the Refuelable interface.
 * It provides functionality for managing the fuel of the jetpack.
 *
 * @author Yehor Nesterenko
 */
public class Jetpack extends Item implements Refuelable {
    /**
     * The maximum fuel capacity of the jetpack.
     */
    protected final float fuelCapacity = 3000;
    /**
     * The current fuel level of the jetpack.
     */
    protected float currentFuel;

    /**
     * Constructor for creating a Jetpack instance with a DragAndDrop instance and a current fuel level.
     *
     * @param name The name of the jetpack.
     * @param weight The weight of the jetpack.
     * @param normalTexturePath The path to the normal texture of the jetpack.
     * @param size The size of the jetpack.
     * @param dragAndDrop The DragAndDrop instance associated with the jetpack.
     * @param currentFuel The current fuel level of the jetpack.
     *
     * @author Yehor Nesterenko
     */
    public Jetpack(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop, int currentFuel) {
        super(name, weight, normalTexturePath, size, dragAndDrop);
        this.currentFuel = currentFuel;
    }

    /**
     * Constructor for creating a Jetpack instance with a DragAndDrop instance.
     *
     * @param name The name of the jetpack.
     * @param weight The weight of the jetpack.
     * @param normalTexturePath The path to the normal texture of the jetpack.
     * @param size The size of the jetpack.
     * @param dragAndDrop The DragAndDrop instance associated with the jetpack.
     *
     * @author Yehor Nesterenko
     */
    public Jetpack(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop){
        super(name, weight, normalTexturePath, size, dragAndDrop);
    }

    /**
     * Constructor for creating a Jetpack instance located in an inventory.
     *
     * @param name The name of the jetpack.
     * @param weight The weight of the jetpack.
     * @param normalTexturePath The path to the normal texture of the jetpack.
     * @param size The size of the jetpack.
     * @param inventory The inventory in which the jetpack is located.
     *
     * @author Yehor Nesterenko
     */
    public Jetpack(String name, float weight, String normalTexturePath, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexturePath, size, inventory);
    }

    /**
     * Refuels the jetpack by a certain amount.
     * If the fuel amount exceeds the fuel capacity, the current fuel level is set to the fuel capacity.
     *
     * @param fuelAmount The amount of fuel to be added.
     *
     * @author Yehor Nesterenko
     */
    public void refuel(float fuelAmount) {
        if(currentFuel + fuelAmount > fuelCapacity){
            currentFuel = fuelCapacity;
            return;
        }
        currentFuel += fuelAmount;
    }

    /**
     * Uses a certain amount of fuel from the jetpack.
     *
     * @param fuelAmount The amount of fuel to be used.
     *
     * @author Yehor Nesterenko
     */
    public void useFuel(float fuelAmount) {
        currentFuel -= fuelAmount;
    }

    /**
     * Checks if the jetpack has fuel.
     *
     * @return A boolean value indicating whether the jetpack has fuel.
     *
     * @author Yehor Nesterenko
     */
    public boolean hasFuel() {
        return currentFuel > 0;
    }

    /**
     * Fuels the jetpack using coal from the player's inventory.
     * If the player has coal in their inventory, the jetpack is refueled by 1000 and the coal is removed from the inventory.
     *
     * @author Yehor Nesterenko
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
