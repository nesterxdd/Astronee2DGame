package com.mygdx.game.item.modules.oxygen;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.world.GameMap;

/**
 * Represents an oxygen station in the game.
 * This class extends the OxygenModule class and provides functionality for managing the fuel of an oxygen station.
 * It also has an installed state which determines if the station is in use.
 *
 * @author Mykola Isaiev
 */
public class OxygenStation extends OxygenModule {
    /**
     * The installed state of the oxygen station.
     */
    boolean installed = false;

    /**
     * Constructor for creating an OxygenStation instance with a DragAndDrop instance.
     * This constructor also sets the fuel capacity and current fuel of the oxygen station.
     *
     * @param name The name of the oxygen station.
     * @param weight The weight of the oxygen station.
     * @param normalTexturePath The path to the normal texture of the oxygen station.
     * @param size The size of the oxygen station.
     * @param dragAndDrop The DragAndDrop instance associated with the oxygen station.
     *
     * @author Mykola Isaiev
     */
    public OxygenStation(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, normalTexturePath, size, dragAndDrop);
        super.setFuelCapacity(10000);
        super.setCurrentFuel(1000);
    }

    /**
     * Constructor for creating an OxygenStation instance located in an inventory.
     *
     * @param name The name of the oxygen station.
     * @param weight The weight of the oxygen station.
     * @param normalTexturePath The path to the normal texture of the oxygen station.
     * @param size The size of the oxygen station.
     * @param inventory The inventory in which the oxygen station is located.
     *
     * @author Mykola Isaiev
     */
    public OxygenStation(String name, float weight, String normalTexturePath, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexturePath, size, inventory);
    }

    /**
     * Performs the actions of the oxygen station in each game tick.
     * If the station is installed and has fuel, it uses a small amount of fuel.
     *
     * @param delta The time in seconds since the last game tick.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void act(float delta) {
        if (installed) {
            if (this.hasFuel()) {
                this.useFuel(0.01f);
            }
        }
    }

    /**
     * Interacts with the oxygen station.
     * The station is installed and added to the active actors.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void interact() {
        installed = true;
        GameMap.getPlayer().getMap().addActActor(this);
    }

    /**
     * Checks if the oxygen station is installed.
     *
     * @return True if the oxygen station is installed, false otherwise.
     *
     * @author Mykola Isaiev
     */
    public boolean isInstalled() {
        return installed;
    }
}
