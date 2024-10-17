package com.mygdx.game.item.interfaces;

/**
 * Represents an object that can be placed in the game.
 * This interface defines a single method, place, which is called to place the object.
 *
 * @author Mykola Isaiev
 */
public interface Placeable {
    /**
     * Defines the placement behavior of the object.
     * The specific behavior is determined by the class that implements this interface.
     *
     * @author Mykola Isaiev
     */
    void place();
}
