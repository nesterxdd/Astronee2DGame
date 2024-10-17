package com.mygdx.game.item.resources;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.interfaces.Placeable;
import com.mygdx.game.world.TileType;

/**
 * Represents a raw resource in the game.
 * This class extends the Resource class and implements the Placeable interface.
 * A raw resource is a type of resource that can be placed in the game world.
 *
 * @author Mykola Isaiev
 */
public class RawResource extends Resource implements Placeable {
    /**
     * @deprecated The type of tile that represents the raw resource in the game world.
     */
    private TileType rawTileType;

    /**
     * Constructor for creating a raw resource with a DragAndDrop instance.
     *
     * @param name The name of the raw resource.
     * @param weight The weight of the raw resource.
     * @param normalTexturePath The path to the normal texture of the raw resource.
     * @param rawTileType The type of tile that represents the raw resource in the game world.
     * @param size The size of the raw resource.
     * @param dragAndDrop The DragAndDrop instance associated with the raw resource.
     *
     * @author Mykola Isaiev
     */
    public RawResource(String name, float weight, String normalTexturePath, TileType rawTileType, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, normalTexturePath, size, dragAndDrop);
        this.rawTileType = rawTileType;
    }

    /**
     * Constructor for creating a raw resource located in an inventory.
     *
     * @param name The name of the raw resource.
     * @param weight The weight of the raw resource.
     * @param normalTexture The normal texture of the raw resource.
     * @param rawTileType The type of tile that represents the raw resource in the game world.
     * @param size The size of the raw resource.
     * @param inventory The inventory in which the raw resource is located.
     *
     * @author Mykola Isaiev
     */
    public RawResource(String name, float weight, String normalTexture, TileType rawTileType, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexture, size, inventory);
        this.rawTileType = rawTileType;
    }
}
