package com.mygdx.game.item.modules.crafting_tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.modules.Module;
import com.mygdx.game.world.GameMap;

/**
 * Represents a workbench in the game.
 * This class extends the Module class and provides functionality for managing the crafting inventory of the workbench.
 *
 * @author Mykola Isaiev
 */
public abstract class Workbench extends Module {
    /**
     * The crafting inventory of the workbench.
     */
    Inventory craftingInventory;

    /**
     * Constructor for creating a Workbench instance with a DragAndDrop instance.
     *
     * @param name The name of the workbench.
     * @param weight The weight of the workbench.
     * @param normalTexturePath The path to the normal texture of the workbench.
     * @param size The size of the workbench.
     * @param dragAndDrop The DragAndDrop instance associated with the workbench.
     *
     * @author Mykola Isaiev
     */
    public Workbench(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, normalTexturePath, size, dragAndDrop);
    }

    /**
     * Constructor for creating a Workbench instance located in an inventory.
     *
     * @param name The name of the workbench.
     * @param weight The weight of the workbench.
     * @param normalTexturePath The path to the normal texture of the workbench.
     * @param size The size of the workbench.
     * @param inventory The inventory in which the workbench is located.
     *
     * @author Mykola Isaiev
     */
    public Workbench(String name, float weight, String normalTexturePath, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexturePath, size, inventory);
    }

    /**
     * Interacts with the workbench.
     * The crafting inventory is made visible and the workbench is added to the active actors.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void interact() {
        craftingInventory.changePositionByCoordinates(GameMap.getPlayer().getX() + 200, GameMap.getPlayer().getY() - 200);
        craftingInventory.setVisible(true);
        GameMap.getPlayer().getMap().addActActor(this);
    }

    /**
     * Performs the actions of the workbench in each game tick.
     * If the ESCAPE key is pressed, the crafting inventory is hidden and the workbench is removed from the active actors.
     *
     * @param delta The time in seconds since the last game tick.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void act(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            craftingInventory.setVisible(false);
            GameMap.getPlayer().getMap().removeActActor(this);
        }
    }
}
