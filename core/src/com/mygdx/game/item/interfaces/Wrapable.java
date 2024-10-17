package com.mygdx.game.item.interfaces;

/**
 * Represents an object that can be wrapped and unwrapped in the game.
 * This interface defines two methods, wrap and unwrap, which are called to wrap and unwrap the object.
 *
 * @author Mykola Isaiev
 */
public interface Wrapable {
    /**
     * Defines the wrapping behavior of the object.
     * The specific behavior is determined by the class that implements this interface.
     *
     * @author Mykola Isaiev
     */
    void wrap();

    /**
     * Defines the unwrapping behavior of the object.
     * The specific behavior is determined by the class that implements this interface.
     *
     * @author Mykola Isaiev
     */
    void unwrap();
}
