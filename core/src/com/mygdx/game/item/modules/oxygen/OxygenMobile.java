package com.mygdx.game.item.modules.oxygen;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;

/**
 * Represents a mobile oxygen module in the game.
 * This class extends the OxygenModule class and provides functionality for managing the fuel of a mobile oxygen module.
 *
 * @author Mykola Isaiev
 */
public class OxygenMobile extends OxygenModule {
    /**
     * Constructor for creating an OxygenMobile instance with a DragAndDrop instance.
     * This constructor also sets the fuel capacity and current fuel of the mobile oxygen module.
     *
     * @param name The name of the mobile oxygen module.
     * @param weight The weight of the mobile oxygen module.
     * @param normalTexturePath The path to the normal texture of the mobile oxygen module.
     * @param size The size of the mobile oxygen module.
     * @param dragAndDrop The DragAndDrop instance associated with the mobile oxygen module.
     *
     * @author Mykola Isaiev
     */
    public OxygenMobile(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, normalTexturePath, size, dragAndDrop);
        super.setFuelCapacity(2000);
        super.setCurrentFuel(0);
    }

    /**
     * Constructor for creating an OxygenMobile instance located in an inventory.
     *
     * @param name The name of the mobile oxygen module.
     * @param weight The weight of the mobile oxygen module.
     * @param normalTexturePath The path to the normal texture of the mobile oxygen module.
     * @param size The size of the mobile oxygen module.
     * @param inventory The inventory in which the mobile oxygen module is located.
     *
     * @author Mykola Isaiev
     */
    public OxygenMobile(String name, float weight, String normalTexturePath, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexturePath, size, inventory);
    }

    /**
     * Interacts with the mobile oxygen module.
     * The specific interaction behavior is not defined in this class and should be implemented in subclasses.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void interact() {

    }
}
