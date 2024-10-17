package com.mygdx.game.item.modules.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entities.Player;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.item.Item;
import com.mygdx.game.gui.FunctionInterface;
import com.mygdx.game.world.TileType;

import java.util.HashMap;

public class Rocket extends Item {

    private static final int ITERATIONS = 600;
    private static final float INTERVALSPEED = 0.02f;

    private boolean isLaunched = false;
    private TextureRegion[] rocketStates;
    private int currentStateIndex = 0;


    public Rocket(String name, float weight, String texturePath, ItemSize size, DragAndDrop dragAndDrop) {
        super(name, weight, texturePath, size, dragAndDrop);
        loadRocketStates();
    }

    public Rocket(String name, float weight, String texturePath, ItemSize size, Inventory inventory) {
        super(name, weight, texturePath, size, inventory);
        loadRocketStates();
    }

    @Override
    public void setWorldMode(Vector2 position) {
        inventorySlot = null;
        if (wrapped) {
            setSize(size.getLowerSize().getId() * TileType.TILE_SIZE, size.getLowerSize().getId() * TileType.TILE_SIZE);
        } else {
            setSize( ItemSize.BIG.getWidth() , ItemSize.BIG.getWidth() );
        }

        setPosition(position.x, position.y);
    }


    private void loadRocketStates() {
        rocketStates = new TextureRegion[8];
        for (int i = 0; i < 8; i++) {
            rocketStates[i] = new TextureRegion(new Texture(Gdx.files.internal("animations/player/rocket/rocket" + i + ".png")));
        }
    }


    @Override
    public void handleWorldClick() {
        super.handleWorldClick();
        if (!isWrapped()) {
            HashMap<String, FunctionInterface> labels = new HashMap<>();
            labels.put("Launch", () -> launch(GameMap.getPlayer()));

            Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            GameMap.getStage().getViewport().unproject(vec);
            GameMap.getPlayer().getMap().getActionsPopUpList().addAction(labels, new Vector2(vec.x, vec.y));
        }
    }

    public boolean isLaunched(){
        return isLaunched;
    }

    private void launch(Player player) {
        isLaunched = true;
        player.getMap().removeActor(player);
        player.setPosition(getX(), getY()); // Set player's position to rocket's position
        player.setInputRestricted(true);

        Timer.schedule(new Task() {
            private int iterations = 0;


            @Override
            public void run() {
                if (iterations < ITERATIONS) {
                    setY(getY() + 3);

                    player.setVelocityY(100);
                    iterations++;
                    currentStateIndex = (currentStateIndex + 1) % rocketStates.length;
                    setRegion(rocketStates[currentStateIndex]);
                } else {
                    isLaunched = false;
                    cancel();

                    player.getMap().clearStage();
                }
            }
        }, 0, INTERVALSPEED);
    }
}
