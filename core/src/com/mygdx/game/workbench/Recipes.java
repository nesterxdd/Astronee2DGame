package com.mygdx.game.workbench;

import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.modules.crafting_tables.BigWorkbench;
import com.mygdx.game.item.modules.crafting_tables.LargeWorkbench;
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
import java.util.Arrays;

/**
 * Represents a collection of recipes in the game.
 * This class provides functionality for setting up and crafting items based on recipes.
 *
 * @author Mykola Isaiev
 */
public class Recipes {
    /**
     * The list of recipes in the game.
     */
    private static ArrayList<Recipe> recipes = new ArrayList<>();

    /**
     * Constructor for creating a Recipes instance.
     * This constructor also sets up the recipes.
     *
     * @author Mykola Isaiev
     */
    public Recipes() {
        setup();
    }

    /**
     * Sets up the recipes in the game.
     *
     * @author Mykola Isaiev
     */
    public void setup() {
        recipes = new ArrayList<>();

        recipes.add(new Recipe("Compound",
                new MediumWorkbench("Medium Workbench", 1, "items/modules/crafting_tables/medium_workbench.png",
                ItemSize.MEDIUM, GameMap.getPlayer().getMap().getDragAndDrop()), true));

        recipes.add(new Recipe(new String[] {"Aluminium", "Aluminium"},
                new BigWorkbench("Big Workbench", 1, "items/modules/crafting_tables/big_workbench.png",
                ItemSize.BIG, GameMap.getPlayer().getMap().getDragAndDrop()), true));

        recipes.add(new Recipe(new String[] {"Iron", "Iron", "Ruby"},
                new LargeWorkbench("Large Workbench", 1, "items/modules/crafting_tables/large_workbench.png",
                ItemSize.LARGE, GameMap.getPlayer().getMap().getDragAndDrop()), true));

        recipes.add(new Recipe(new String[] {"Resin", "Resin"},
                new MediumStorage("Medium Storage", 1, "items/modules/storages/medium_storage.png",
                ItemSize.MEDIUM, GameMap.getPlayer().getMap().getDragAndDrop()), true));

        recipes.add(new Recipe(new String[] {"Iron", "Iron", "Resin"},
                new BigStorage("Big Storage", 1, "items/modules/storages/big_storage.png",
                ItemSize.BIG, GameMap.getPlayer().getMap().getDragAndDrop()), true));

        recipes.add(new Recipe(new String[] {"Ruby", "Ruby", "Resin", "Resin"},
                new LargeStorage("Large Storage", 1, "items/modules/storages/large_storage.png",
                        ItemSize.LARGE, GameMap.getPlayer().getMap().getDragAndDrop()), true));

        recipes.add(new Recipe(new String[] {"Aluminium", "Copper"},
                new Jetpack("Jetpack", 1, "items/modules/utils/jetpack.png",
                ItemSize.SMALL, GameMap.getPlayer().getMap().getDragAndDrop()), true));

        recipes.add(new Recipe("Resin",
                new OxygenStation("Oxygen Station", 1, "items/modules/oxygen/station.png",
                ItemSize.MEDIUM, GameMap.getPlayer().getMap().getDragAndDrop()), true));

        recipes.add(new Recipe(new String[] {"Ruby", "Ruby", "Compound", "Compound"},
                new OxygenMobile("Oxygen Mobile", 1, "items/modules/oxygen/mobile.png",
                ItemSize.MEDIUM, GameMap.getPlayer().getMap().getDragAndDrop()), true));

        recipes.add(new Recipe("Compound",
                new SmallSoilStorage("Small Soil Storage", 1, "items/storages/SmallSoilStorage.png",
                ItemSize.SMALL, GameMap.getPlayer().getMap().getDragAndDrop(), 0), true));

        recipes.add(new Recipe(new String[] {"Ruby", "Ruby", "Coal", "Coal"},
                new Rocket("Rocket", 1, "items/modules/utils/rocket.png",
                ItemSize.LARGE, GameMap.getPlayer().getMap().getDragAndDrop()), true));
    }

    /**
     * Crafts an item based on a recipe.
     *
     * @param item The name of the item to be crafted.
     * @param inventory The inventory where the crafted item will be added.
     *
     * @author Mykola Isaiev
     */
    public static void craft(String item, Inventory inventory) {
        for (Recipe recipe : recipes) {
            if (recipe.result.getName().equals(item)) {
                Item result = recipe.craft(inventory);
                if (result != null) {
                    for (String source : recipe.source) {
                        inventory.removeItem(source);
                    }
                    inventory.addItem(result);
                } else {
                    return;
                }
            }
        }
    }

    /**
     * Represents a recipe in the game.
     * This class provides functionality for crafting items based on a recipe.
     *
     * @author Mykola Isaiev
     */
    private class Recipe {
        /**
         * The list of source items required for the recipe.
         */
        private ArrayList<String> source;
        /**
         * The result item of the recipe.
         */
        private Item result;

        /**
         * Constructor for creating a Recipe instance with multiple source items.
         *
         * @param source The array of source items required for the recipe.
         * @param result The result item of the recipe.
         * @param wrapped A boolean value indicating whether the result item should be wrapped.
         *
         * @author Mykola Isaiev
         */
        public Recipe(String[] source, Item result, boolean wrapped) {
            this.source = new ArrayList<>();
            this.source.addAll(Arrays.asList(source));
            this.result = result;

            if (wrapped) {
                this.result.wrap();
                this.result.setInventoryMode();
            }
        }

        /**
         * Constructor for creating a Recipe instance with a single source item.
         *
         * @param source The source item required for the recipe.
         * @param result The result item of the recipe.
         * @param wrapped A boolean value indicating whether the result item should be wrapped.
         *
         * @author Mykola Isaiev
         */
        public Recipe(String source, Item result, boolean wrapped) {
            this.source = new ArrayList<>();
            this.source.add(source);
            this.result = result;

            if (wrapped) {
                this.result.wrap();
                this.result.setInventoryMode();
            }
        }

        /**
         * Crafts an item based on the recipe.
         *
         * @param inventory The inventory where the crafted item will be added.
         * @return The crafted item if the recipe can be crafted, null otherwise.
         *
         * @author Mykola Isaiev
         */
        public Item craft(Inventory inventory) {
            if (verify(inventory)) {
                return result;
            }
            return null;
        }

        /**
         * Verifies if the recipe can be crafted based on the items in the inventory.
         *
         * @param inventory The inventory to be checked.
         * @return A boolean value indicating whether the recipe can be crafted.
         *
         * @author Mykola Isaiev
         */
        private boolean verify(Inventory inventory) {
            ArrayList<String> inventoryItems = inventory.getItems();

            for (String item : source) {
                if (!inventoryItems.contains(item)) {
                    return false;
                } else {
                    inventoryItems.remove(item);
                }
            }

            return true;
        }
    }
}
