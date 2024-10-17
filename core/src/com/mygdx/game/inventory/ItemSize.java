package com.mygdx.game.inventory;

/**
 * Represents the size of an item in the game.
 * This enum is used to define the width and height of an item, as well as its comparative size.
 *
 * @author Mykola Isaiev
 */
public enum ItemSize {
    SMALL(1, 90, 90),
    MEDIUM(2, 120, 120),
    BIG(3, 150, 150),
    LARGE(4, 180, 180);

    /**
     * The width of the item.
     */
    private int width;
    /**
     * The height of the item.
     */
    private int height;
    /**
     * The comparative size of the item.
     */
    private int id;

    /**
     * Constructs a new ItemSize enum with a specified id, width, and height.
     *
     * @param id the comparative size of the item
     * @param width the width of the item
     * @param height the height of the item
     */
    ItemSize(int id, int width, int height){
        this.id = id;
        this.width = width;
        this.height = height;
    }

    /**
     * Retrieves the width of the item.
     *
     * @return the width of the item
     *
     * @author Mykola Isaiev
     */
    public int getWidth(){
        return width;
    }

    /**
     * Retrieves the height of the item.
     *
     * @return the height of the item
     *
     * @author Mykola Isaiev
     */
    public int getHeight(){
        return height;
    }

    /**
     * Retrieves the comparative size of the item.
     *
     * @return the comparative size of the item
     *
     * @author Mykola Isaiev
     */
    public int getId() {
        return id;
    }

    /**
     * Compares this item size with another item size.
     *
     * @param size the other item size
     * @return 1 if this item size is larger, -1 if it's smaller, and 0 if they're equal
     *
     * @author Mykola Isaiev
     */
    public int compare(ItemSize size) {
        if (this.id > size.id) {
            return 1;
        } else if (this.id < size.id) {
            return -1;
        }
        return 0;
    }

    /**
     * Retrieves the next lower item size.
     *
     * @return the next lower item size
     *
     * @author Mykola Isaiev
     */
    public ItemSize getLowerSize() {
        switch (this) {
            case SMALL:
            case MEDIUM:
                return SMALL;
            case BIG:
                return MEDIUM;
            case LARGE:
                return BIG;
            default:
                return null;
        }
    }
}
