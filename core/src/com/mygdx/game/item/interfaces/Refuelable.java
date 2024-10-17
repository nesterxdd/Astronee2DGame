package com.mygdx.game.item.interfaces;

/**
 * Represents an object that can be refueled in the game.
 * This interface defines a single method, fuel, which is called to refuel the object.
 * 
 * @author Yehor Nesterenko
 */
public interface Refuelable {
    /**
     * Defines the refueling behavior of the object.
     * The specific behavior is determined by the class that implements this interface.
     * 
     * @author Yehor Nesterenko
     */
    void fuel();
}
