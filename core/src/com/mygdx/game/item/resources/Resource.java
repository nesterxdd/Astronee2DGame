package com.mygdx.game.item.resources;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.Item;

/**
 * Represents a resource in the game.
 * This abstract class extends the Item class and provides a base for all resources in the game.
 *
 * @author Mykola Isaiev
 */
public abstract class Resource extends Item {

    /**
     * Constructor for creating a resource with a DragAndDrop instance.
     *
     * @param name The name of the resource.
     * @param weight The weight of the resource.
     * @param normalTexturePath The path to the normal texture of the resource.
     * @param size The size of the resource.
     * @param dragAndDrop The DragAndDrop instance associated with the resource.
     *
     * @author Mykola Isaiev
     */
    public Resource(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, normalTexturePath, size, dragAndDrop);
    }

    /**
     * Constructor for creating a resource located in an inventory.
     *
     * @param name The name of the resource.
     * @param weight The weight of the resource.
     * @param normalTexture The normal texture of the resource.
     * @param size The size of the resource.
     * @param inventory The inventory in which the resource is located.
     *
     * @author Mykola Isaiev
     */
    public Resource(String name, float weight, String normalTexture, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexture, size, inventory);
    }
}
