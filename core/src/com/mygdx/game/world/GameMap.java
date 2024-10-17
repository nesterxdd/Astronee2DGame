package com.mygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.entities.Player;
import com.mygdx.game.gui.ActionsPopUpList;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.modules.crafting_tables.LargeWorkbench;
import com.mygdx.game.item.modules.oxygen.OxygenStation;
import com.mygdx.game.item.PlaceableItem;
import com.mygdx.game.item.resources.RawResource;
import com.mygdx.game.workbench.Recipes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a game map.
 * This class provides the structure for managing a game map.
 * It includes methods for rendering the map, updating the map, and interacting with the tiles on the map.
 * This class is abstract and must be extended by a specific type of game map.
 *
 * @author Mykola Isaiev
 * @author Yehor Nesterenko
 * @author Danylo Kost
 */
public abstract class GameMap {
    /**
     * Stage where all actors are added for rendering and interaction.
     */
    static Stage stage;

    /**
     * Player instance representing the user's character in the game.
     */
    static Player player;

    /**
     * DragAndDrop instance used for handling drag and drop functionality in the game.
     */
    DragAndDrop dragAndDrop;

    /**
     * List of actors that are currently active in the game.
     */
    ArrayList<Actor> actActors;

    /**
     * ActionsPopUpList instance used for displaying a list of actions in the game.
     */
    ActionsPopUpList actionsPopUpList;

    /**
     * Boolean flag indicating if a placeable item exists in the game.
     */
    boolean placeableItemExists = false;

    /**
     * Recipes instance containing all the recipes available in the game.
     */
    Recipes recipes;

    /**
     * Animation for the breaking effect in the game.
     */
    private Animation<TextureRegion> breakAnimation;

    public static boolean cleared = false;

    /**
     * Constructor for creating a GameMap instance.
     * This constructor also sets up the player, stage, and inventory.
     */
    public GameMap() {
        dragAndDrop = new DragAndDrop();

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera()));
        player = new Player(2500, 3000, this, new Inventory(dragAndDrop));

        recipes = new Recipes();

        Gdx.input.setInputProcessor(stage);

        Item oxygenStation = new OxygenStation("Oxygen Station", 1, "items/modules/oxygen/station.png", ItemSize.MEDIUM, dragAndDrop);
        oxygenStation.wrap();
        oxygenStation.setInventoryMode();
        player.getInventory().addItem(oxygenStation);

        actionsPopUpList = new ActionsPopUpList();
        stage.addActor(player);
        stage.addActor(player.getInventory());
        stage.addActor(actionsPopUpList);
        actActors = new ArrayList<>();

        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i <= 5; i++) {
            frames.add(new TextureRegion(new Texture(Gdx.files.internal("break" + i + ".png"))));
        }
        breakAnimation = new Animation<>(0.13f, frames, Animation.PlayMode.NORMAL);
    }

    /**
     * Returns the animation for the breaking effect in the game.
     *
     * @return The animation for the breaking effect.
     *
     * @author Yehor Nesterenko
     */
    public Animation<TextureRegion> getBreakAnimation() {
        return breakAnimation;
    }

    /**
     * Finds the highest point on the map at a given x-coordinate.
     * This method iterates from the top of the map downwards until it finds a collidable tile.
     * It then returns the y-coordinate of this tile, representing the highest point at the given x-coordinate.
     *
     * @param worldX The x-coordinate to find the highest point at.
     * @return The y-coordinate of the highest point at the given x-coordinate.
     *
     * @author Mykola Isaiev
     */
    public int findHighestPoint(float worldX) {
        int x = (int) (worldX / TileType.TILE_SIZE);
        for (int worldY = (int) (getPixelHeight() - 1); worldY >= 0; worldY--) {
            int y = (int) (worldY / TileType.TILE_SIZE);
            for (int layer = 0; layer < getLayers(); layer++) {
                for (int xOffset = -1; xOffset <= 1; xOffset++) {
                    if (x + xOffset < 0 || x + xOffset >= getWidth()) {
                        continue;
                    }
                    TileType type = getTileTypeByCoordinate(layer, x + xOffset, y);
                    if (type != null && type.isCollidable()) {
                        return worldY;
                    }
                }
            }
        }
        return 0;
    }


    /**
     * Calculates the number of tiles in a specified area that can be interacted with.
     * Interactable tiles are those that are either collidable or extractable.
     *
     * @param coordinates The coordinates of the top-left corner of the area.
     * @param width The width of the area.
     * @param height The height of the area.
     * @return The number of interactable tiles in the specified area.
     *
     * @author Yehor Nesterenko
     */
    public double getInteractableTilesAmount(Vector2 coordinates, int width, int height) {
        int interactableTiles = 0;

        for (int row = (int) (coordinates.y / TileType.TILE_SIZE); row < Math.ceil((coordinates.y + height) / TileType.TILE_SIZE); row++) {
            for (int col = (int) (coordinates.x / TileType.TILE_SIZE); col < Math.ceil((coordinates.x + width) / TileType.TILE_SIZE); col++) {
                for (int layer = 0; layer < getLayers(); layer++) {
                    TileType type = getTileTypeByCoordinate(layer, col, row);
                    if (type != null) {
                        if (type.isCollidable() || type.isExtractable()) {
                            interactableTiles++;
                        }
                    }
                }
            }
        }
        return interactableTiles;
    }



    //make method to remove all stage actors
    public void clearStage() {
        cleared = true;
        stage.clear();
    }

    public int setTilesInAreaWithoutOverlay(Vector2 coordinates, int width, int height, TileType tileType, int maxTiles) {
        int placedTiles = 0;

        // Check bounds
        if (coordinates.x < 0 || coordinates.y < 0 || coordinates.x + width > getPixelWidth() || coordinates.y + height > getPixelHeight()) {
            return placedTiles; // Return 0 as no tiles can be placed out of bounds
        }

        int startX = (int) (coordinates.x / TileType.TILE_SIZE);
        int startY = (int) (coordinates.y / TileType.TILE_SIZE);
        int endX = (int) Math.ceil((coordinates.x + width) / TileType.TILE_SIZE);
        int endY = (int) Math.ceil((coordinates.y + height) / TileType.TILE_SIZE);

        // Only work with layer 1 (assuming layer 0 is reserved and should not be touched)
        for (int row = startY; row < endY; row++) {
            for (int col = startX; col < endX; col++) {
                if (placedTiles >= maxTiles) {
                    return placedTiles;
                }

                TileType currentType = getTileTypeByCoordinate(1, col, row);

                if (currentType == null || !currentType.isCollidable()) {
                    setTileByCoordinate(1, col, row, tileType);
                    placedTiles++;
                }
            }
        }

        return placedTiles;
    }

    /**
     * Sets tiles in a specified area, overlaying existing collidable tiles.
     * This method will replace all collidable tiles in the specified area with the specified tile type.
     *
     * @param coordinates The coordinates of the top-left corner of the area.
     * @param width The width of the area.
     * @param height The height of the area.
     * @param tileType The type of tile to place.
     * @return The number of tiles that were replaced.
     *
     * @author Yehor Nesterenko
     */
    public int setTilesWithOverlay(Vector2 coordinates, int width, int height, TileType tileType) {
        int removedTiles = 0;

        // Check bounds
        if (coordinates.x < 0 || coordinates.y < 0 || coordinates.x + width > getPixelWidth() || coordinates.y + height > getPixelHeight()) {
            return removedTiles;
        }

        int startX = (int) (coordinates.x / TileType.TILE_SIZE);
        int startY = (int) (coordinates.y / TileType.TILE_SIZE);
        int endX = (int) Math.ceil((coordinates.x + width) / TileType.TILE_SIZE);
        int endY = (int) Math.ceil((coordinates.y + height) / TileType.TILE_SIZE);

        for (int row = startY; row < endY; row++) {
            for (int col = startX; col < endX; col++) {
                for(int layer = 0; layer < getLayers(); layer++){
                    TileType currentType = getTileTypeByCoordinate(layer, col, row);

                    if (currentType != null && currentType.isCollidable() ) {
                        setTileByCoordinate(layer, col, row, tileType);
                        removedTiles++;
                    }
                }
            }
        }

        return removedTiles;
    }

    /**
     * Renders the map and all entities on it.
     *
     * @param batch The SpriteBatch used to render the map.
     *
     * @author Danylo Kost
     */
    public void render(SpriteBatch batch) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.draw();
    }

    /**
     * Resizes the map.
     *
     * @param width The new width of the map.
     * @param height The new height of the map.
     *
     * @author Danylo Kost
     */
    public void resize(int width, int height){

    }

    /**
     * Updates the map and all entities on it.
     *
     * @param delta The time since the last update.
     *
     * @author Danylo Kost
     */
    public void update(float delta) {
        player.act(delta);
        player.updateCursorPosition((FitViewport) stage.getViewport());

        ((OrthographicCamera)stage.getCamera()).zoom = player.getCamZoom()/100f;
        ((OrthographicCamera)stage.getCamera()).position.set(player.getX(), player.getY(), 0);

        //не менять на сокращенный фор иначе все пиздой накроется
        for (int i = 0; i < actActors.size(); i++) {
            actActors.get(i).act(delta);
        }
    }

    /**
     * Returns the type of the tile at the specified location in the specified layer.
     *
     * @param layer The layer of the tile to get.
     * @param x The x-coordinate of the tile to get.
     * @param y The y-coordinate of the tile to get.
     * @return The type of the tile at the specified location in the specified layer.
     *
     * @author Mykola Isaiev
     */

    /**
     * Returns the type of the tile at the specified location in the specified layer.
     *
     * @param layer The layer of the tile to get.
     * @param x The x-coordinate of the tile to get.
     * @param y The y-coordinate of the tile to get.
     * @return The type of the tile at the specified location in the specified layer.
     *
     * @author Mykola Isaiev
     */
    public abstract TileType getTileTypeByLocation(int layer, float x, float y);

    /**
     * Disposes of the map and all resources associated with it.
     * This method should be called when the map is no longer needed.
     *
     * @author Mykola Isaiev
     */
    public abstract void dipsose();

    /**
     * Returns the type of the tile at the specified coordinate in the specified layer.
     *
     * @param layer The layer of the tile to get.
     * @param col The column of the tile to get.
     * @param row The row of the tile to get.
     * @return The type of the tile at the specified coordinate in the specified layer.
     *
     * @author Mykola Isaiev
     */
    public abstract TileType getTileTypeByCoordinate(int layer, int col, int row);

    /**
     * Checks if a rectangle collides with the map.
     * This method checks if the specified rectangle is out of bounds or collides with any collidable tiles in the map.
     *
     * @param x The x-coordinate of the top-left corner of the rectangle.
     * @param y The y-coordinate of the top-left corner of the rectangle.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     * @return True if the rectangle collides with the map, false otherwise.
     *
     * @author Mykola Isaiev
     */
    public boolean doesRectCollideWithMap(float x, float y, int width, int height){
        if (x < 0 || y < 0 || x + width > getPixelWidth() || y + height > getPixelHeight()){
            return true;
        }

        for (int row = (int) (y / TileType.TILE_SIZE); row < Math.ceil((y + height) / TileType.TILE_SIZE); row++){
            for (int col = (int) (x / TileType.TILE_SIZE); col < Math.ceil((x + width) / TileType.TILE_SIZE); col++){
                for (int layer = 0; layer < getLayers(); layer++){
                    TileType type = getTileTypeByCoordinate(layer, col, row);
                    if (type != null && type.isCollidable()){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns the width of the map in tiles.
     *
     * @return The width of the map in tiles.
     *
     * @author Mykola Isaiev
     */
    public abstract float getWidth();

    /**
     * Returns the height of the map in tiles.
     *
     * @return The height of the map in tiles.
     *
     * @author Mykola Isaiev
     */
    public abstract float getHeight();

    /**
     * Returns the number of layers in the map.
     *
     * @return The number of layers in the map.
     *
     * @author Mykola Isaiev
     */
    public abstract int getLayers();

    /**
     * Returns the width of the map in pixels.
     * This is calculated by multiplying the width of the map in tiles by the size of a tile.
     *
     * @return The width of the map in pixels.
     *
     * @author Mykola Isaiev
     */
    public float getPixelWidth() {
        return this.getWidth() * TileType.TILE_SIZE;
    }

    /**
     * Returns the height of the map in pixels.
     * This is calculated by multiplying the height of the map in tiles by the size of a tile.
     *
     * @return The height of the map in pixels.
     *
     * @author Mykola Isaiev
     */
    public float getPixelHeight() {
        return this.getHeight() * TileType.TILE_SIZE;
    }

    /**
     * Sets the tile at the specified coordinate in the specified layer to the specified type.
     *
     * @param layer The layer of the tile to set.
     * @param col The column of the tile to set.
     * @param row The row of the tile to set.
     * @param type The type to set the tile to.
     *
     * @author Mykola Isaiev
     */
    public abstract void setTileByCoordinate(int layer, int col, int row, TileType type);

    /**
     * Sets the tile at the specified location in the specified layer to the specified type.
     *
     * @param layer The layer of the tile to set.
     * @param x The x-coordinate of the tile to set.
     * @param y The y-coordinate of the tile to set.
     * @param type The type to set the tile to.
     *
     * @author Mykola Isaiev
     */
    public abstract void setTileByLocation(int layer, float x, float y, TileType type);

    /**
     * Adds an actor to the list of actors that are currently active in the game.
     * If the actor is a placeable item, it is also added to the stage for rendering and interaction.
     *
     * @param item The actor to add.
     *
     * @author Mykola Isaiev
     */
    public void addActActor(Actor item) {
        if (item instanceof PlaceableItem) {
            placeableItemExists = true;
            stage.addActor(item);
        }
        actActors.add(item);
    }

    /**
     * Removes an actor from the list of actors that are currently active in the game.
     * If the actor is a placeable item, it is also removed from the stage.
     *
     * @param item The actor to remove.
     *
     * @author Mykola Isaiev
     */
    public void removeActActor(Actor item) {
        if (item instanceof PlaceableItem) {
            placeableItemExists = false;
            item.remove();
        }
        actActors.remove(item);
    }

    /**
     * Checks if a placeable item exists in the game.
     *
     * @return True if a placeable item exists, false otherwise.
     *
     * @author Mykola Isaiev
     */
    public boolean isPlaceableItemExists() {
        return placeableItemExists;
    }

    /**
     * Returns the ActionsPopUpList instance used for displaying a list of actions in the game.
     *
     * @return The ActionsPopUpList instance.
     *
     * @author Mykola Isaiev
     */
    public ActionsPopUpList getActionsPopUpList() {
        return actionsPopUpList;
    }

    /**
     * Adds an actor to the stage for rendering and interaction.
     *
     * @param actor The actor to add.
     *
     * @author Mykola Isaiev
     */
    public void addActor(Actor actor) {
        stage.addActor(actor);
    }

    /**
     * Removes an actor from the stage.
     *
     * @param actor The actor to remove.
     *
     * @author Mykola Isaiev
     */
    public void removeActor(Actor actor) {
        actor.remove();
    }

    /**
     * Returns the player instance representing the user's character in the game.
     *
     * @return The player instance.
     *
     * @author Mykola Isaiev
     */
    public static Player getPlayer() {
        return player;
    }

    /**
     * Returns the stage where all actors are added for rendering and interaction.
     *
     * @return The stage where all actors are added.
     *
     * @author Danylo Kost
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Checks and counts the number of extractable resources in a specified area.
     * This method iterates over all tiles in the specified area and counts the number of each type of extractable resource.
     *
     * @param coordinates The coordinates of the top-left corner of the area.
     * @param width The width of the area.
     * @param height The height of the area.
     * @return A HashMap where the keys are the types of extractable resources and the values are the counts of each resource.
     *
     * @author Yehor Nesterenko
     */
    public HashMap<TileType, Integer> checkHowManyExtractableResourcesInArea(Vector2 coordinates, int width, int height){
        HashMap<TileType, Integer> extractableTileCounts = new HashMap<>();

        if (coordinates.x < 0 || coordinates.y < 0 || coordinates.x + width > getPixelWidth() || coordinates.y + height > getPixelHeight()) {
            return extractableTileCounts;
        }

        for (int row = (int) (coordinates.y / TileType.TILE_SIZE); row < Math.ceil((coordinates.y + height) / TileType.TILE_SIZE); row++) {
            for (int col = (int) (coordinates.x / TileType.TILE_SIZE); col < Math.ceil((coordinates.x + width) / TileType.TILE_SIZE); col++) {
                for (int layer = 0; layer < getLayers(); layer++) {
                    TileType type = getTileTypeByCoordinate(layer, col, row);

                    if (type != null && type.isExtractable()) {
                        extractableTileCounts.put(type, extractableTileCounts.getOrDefault(type, 0) + 1);
                    }
                }
            }
        }
        return extractableTileCounts;
    }

    /**
     * Returns the DragAndDrop instance used for handling drag and drop functionality in the game.
     *
     * @return The DragAndDrop instance.
     *
     * @author Mykola Isaiev
     */
    public DragAndDrop getDragAndDrop() {
        return dragAndDrop;
    }

    /**
     * Returns the coordinates of all Oxygen Stations in the game.
     * This method iterates over all active actors in the game and adds the coordinates of any Oxygen Stations to the list.
     *
     * @return An ArrayList of Vector2 objects representing the coordinates of all Oxygen Stations in the game.
     *
     * @author Mykola Isaiev
     */
    public ArrayList<Vector2> getOxygenStationsCoordinates() {
        ArrayList<Vector2> oxygenStationsCoordinates = new ArrayList<>();

        for (Actor actor : actActors) {
            if (actor instanceof OxygenStation) {
                oxygenStationsCoordinates.add(new Vector2(actor.getX(), actor.getY()));
            }
        }

        return oxygenStationsCoordinates;
    }
}
