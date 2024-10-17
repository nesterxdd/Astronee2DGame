package com.mygdx.game.item.interfaces;

/**
 * Represents an object that can be interacted with in the game.
 * This interface defines a single method, interact, which is called when the object is interacted with.
 *
 * @author Mykola Isaiev
 */
public interface Interactable {
    /**
     * Defines the interaction behavior of the object.
     * The specific behavior is determined by the class that implements this interface.
     *
     * @author Mykola Isaiev
     */
    void interact();
}
