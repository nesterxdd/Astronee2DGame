package com.mygdx.game.workbench;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.InventorySlot;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.modules.crafting_tables.LargeWorkbench;
import com.mygdx.game.item.modules.crafting_tables.BigWorkbench;
import com.mygdx.game.item.modules.crafting_tables.MediumWorkbench;
import com.mygdx.game.item.modules.oxygen.OxygenMobile;
import com.mygdx.game.item.modules.oxygen.OxygenStation;
import com.mygdx.game.item.modules.soil_storages.SmallSoilStorage;
import com.mygdx.game.item.modules.storages.BigStorage;
import com.mygdx.game.item.modules.storages.LargeStorage;
import com.mygdx.game.item.modules.storages.MediumStorage;
import com.mygdx.game.item.modules.utils.Jetpack;
import com.mygdx.game.item.modules.utils.Rocket;
import com.mygdx.game.world.GameMap;

import java.util.ArrayList;

/**
 * Represents an inventory workbench in the game.
 * This class extends the Group class and provides functionality for managing items in the workbench.
 *
 * @author Mykola Isaiev
 */
public class InventoryWorkbench extends Group {
    /**
     * The crafting slot in the workbench.
     */
    private InventorySlot craftingSlot;

    /**
     * The list of items in the workbench.
     */
    private ArrayList<Item> items;

    /**
     * The index of the current item in the workbench.
     */
    private int i;

    /**
     * The inventory associated with the workbench.
     */
    private Inventory inventory;

    /**
     * Constructor for creating an inventory workbench with a crafting slot, a DragAndDrop instance, and an inventory.
     *
     * @param craftingSlot The crafting slot in the workbench.
     * @param dragAndDrop The DragAndDrop instance associated with the workbench.
     * @param inventory The inventory associated with the workbench.
     *
     * @author Mykola Isaiev
     */
    public InventoryWorkbench(InventorySlot craftingSlot, DragAndDrop dragAndDrop, Inventory inventory) {
        this.craftingSlot = craftingSlot;
        this.inventory = inventory;
        items = new ArrayList<>();

        setup();
    }

    /**
     * Returns the inventory slot in the workbench.
     *
     * @return The inventory slot in the workbench.
     *
     * @author Mykola Isaiev
     */
    public InventorySlot getInventorySlot() {
        return craftingSlot;
    }

    /**
     * Sets up the workbench with items based on the maximum size of the inventory.
     *
     * @author Mykola Isaiev
     */
    private void setup() {
        switch (inventory.hasMaxSize()) {
            case SMALL:
                items.add(new MediumWorkbench("Medium Workbench", 1, "items/modules/crafting_tables/medium_workbench.png",
                        ItemSize.SMALL, inventory));

                items.add(new OxygenStation("Oxygen Station", 1, "items/modules/oxygen/mobile.png",
                        ItemSize.SMALL, inventory));

                items.add(new SmallSoilStorage("Small Soil Storage", 1, "items/storages/SmallSoilStorage.png",
                        ItemSize.SMALL, inventory, 0));
                break;
            case MEDIUM:
                items.add(new BigWorkbench("Big Workbench", 1, "items/modules/crafting_tables/big_workbench.png",
                        ItemSize.SMALL, inventory));

                items.add(new MediumStorage("Medium Storage", 1, "items/modules/storages/medium_storage.png",
                        ItemSize.SMALL, inventory));

                items.add(new Jetpack("Jetpack", 1, "items/modules/utils/jetpack.png",
                        ItemSize.SMALL, inventory));
                break;
            case BIG:
                items.add(new LargeWorkbench("Large Workbench", 1, "items/modules/crafting_tables/large_workbench.png",
                        ItemSize.SMALL, inventory));

                items.add(new BigStorage("Big Storage", 1, "items/modules/storages/big_storage.png",
                        ItemSize.SMALL, inventory));
                break;
            case LARGE:
                items.add(new LargeStorage("Large Storage", 1, "items/modules/storages/large_storage.png",
                        ItemSize.SMALL, inventory));

                items.add(new OxygenMobile("Oxygen Mobile", 1, "items/modules/oxygen/mobile.png",
                        ItemSize.SMALL, inventory));

                items.add(new Rocket("Rocket", 1, "items/modules/utils/rocket.png",
                        ItemSize.SMALL, inventory));
                break;
        }

        if (!items.isEmpty()) {
            inventory.addItem(items.get(i), craftingSlot);
        }

        Arrow leftArrow = new Arrow("workbench/arrowLeft.png",
                new Vector2(craftingSlot.getX() - 100, craftingSlot.getY()));
        leftArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (items.isEmpty()) {
                    return;
                }
                inventory.removeItem(items.get(i));

                if (i == 0) {
                    i = items.size() - 1;
                } else {
                    i--;
                }

                inventory.addItem(items.get(i), craftingSlot);
            }
        });
        addActor(leftArrow);

        Arrow rightArrow = new Arrow("workbench/arrowRight.png",
                new Vector2(craftingSlot.getX() + 100, craftingSlot.getY()));
        rightArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (items.isEmpty()) {
                    return;
                }
                inventory.removeItem(items.get(i));

                if (i == items.size() - 1) {
                    i = 0;
                } else {
                    i++;
                }

                inventory.addItem(items.get(i), craftingSlot);
            }
        });

        addActor(rightArrow);
    }
}
