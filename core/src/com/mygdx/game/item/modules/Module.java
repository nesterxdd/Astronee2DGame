package com.mygdx.game.item.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.gui.FunctionInterface;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.interfaces.Refuelable;
import com.mygdx.game.item.modules.oxygen.OxygenStation;
import com.mygdx.game.world.GameMap;

import java.util.HashMap;

/**
 * Represents a module in the game.
 * This abstract class extends the Item class and provides a base for all modules in the game.
 *
 * @author Mykola Isaiev
 */
public abstract class Module extends Item {
    /**
     * Constructor for creating a module with a DragAndDrop instance.
     *
     * @param name The name of the module.
     * @param weight The weight of the module.
     * @param normalTexturePath The path to the normal texture of the module.
     * @param size The size of the module.
     * @param dragAndDrop The DragAndDrop instance associated with the module.
     *
     * @author Mykola Isaiev
     */
    public Module(String name, float weight, String normalTexturePath, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, normalTexturePath, size, dragAndDrop);
    }

    /**
     * Constructor for creating a module located in an inventory.
     *
     * @param name The name of the module.
     * @param weight The weight of the module.
     * @param normalTexturePath The path to the normal texture of the module.
     * @param size The size of the module.
     * @param inventory The inventory in which the module is located.
     *
     * @author Mykola Isaiev
     */
    public Module(String name, float weight, String normalTexturePath, ItemSize size, Inventory inventory) {
        super(name, weight, normalTexturePath, size, inventory);
    }

    /**
     * Handles the click event in the game world for the module.
     * This method is overridden from the Item class.
     *
     * @author Mykola Isaiev
     */
    @Override
    public void handleWorldClick() {
        HashMap<String, FunctionInterface> labels = new HashMap<>();


        if (this instanceof OxygenStation && ((OxygenStation) this).isInstalled()) {
            labels.put("Fuel", () -> {
                ((Refuelable) this).fuel();
            });
        } else {
            if ((!wrapped && size == ItemSize.SMALL) || (wrapped && size.getLowerSize() == ItemSize.SMALL)) {
                labels.put("Pick up", () -> {
                    this.pickUp();
                });
            }

            labels.put("Move", () -> {
                this.move();
            });

            if (!wrapped) {
                labels.put("Wrap", () -> {
                    this.wrap();
                });

                if (!(this instanceof OxygenStation)) {
                    labels.put("Open", () -> {
                        this.interact();
                    });
                } else {
                    if (!((OxygenStation) this).isInstalled()) {
                        labels.put("Install", () -> {
                            this.interact();
                        });
                    }
                }

            } else {
                labels.put("Unwrap", () -> {
                    this.unwrap();
                });
            }
        }

        Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        GameMap.getStage().getViewport().unproject(vec);
        GameMap.getPlayer().getMap().getActionsPopUpList().addAction(labels, new Vector2(vec.x, vec.y));
    }

    /**
     * Defines the interaction of the module.
     * This method is abstract and must be implemented in any class that extends Module.
     *
     * @author Mykola Isaiev
     */
    public abstract void interact();
}
