package com.mygdx.game.item.interfaces;

/**
 * Represents an object that can be moved in the game.
 * This interface defines a single method, move, which is called to move the object.
 *
 * @author Mykola Isaiev
 */
public interface Movable {
    /**
     * Defines the movement behavior of the object.
     * The specific behavior is determined by the class that implements this interface.
     *
     * @author Mykola Isaiev
     */
    void move();
}
