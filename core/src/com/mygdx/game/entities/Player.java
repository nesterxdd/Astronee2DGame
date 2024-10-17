package com.mygdx.game.entities;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.inventory.Inventory;
import com.mygdx.game.inventory.ItemSize;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.modules.oxygen.OxygenMobile;
import com.mygdx.game.item.modules.utils.Jetpack;
import com.mygdx.game.item.modules.soil_storages.SoilStorage;
import com.mygdx.game.item.resources.RawResource;
import com.mygdx.game.item.modules.soil_storages.SmallSoilStorage;
import com.mygdx.game.screen.MenuScreen;
import com.mygdx.game.world.GameMap;
import com.mygdx.game.world.TileType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;

public class Player extends Actor {

    /** Player`s horizontal speed.
     */
    private static int SPEED = 150;
    /** Player`s vertical velocity.
     */
    private static final int JUMP_VELOCITY = 300;
    /** Gravity force which applies to player.
     */
    private static final int GRAVITY = -600;
    /** Player`s horizontal velocity with jetpack.
     */
    private static final int JETPACK_VELOCITY = 200;
    /** Value which gives the possibility to play to jump and run.
     * Decreasing while running/jumping and increasing while standing.
     * Doesn`t decrease or increase during normal movement.
     */
    private int stamina = 100;
    /** Player`s state of being on the ground.
     */
    private boolean grounded = false;
    /** Player`s state of being in drill mode.
     */
    private boolean drillMode = false;
    /** Player`s vertical speed.
     */
    private float velocityY = 0;
    /** Player`s camera zoom value.
     */
    private int cameraZoom = 100;
    /** Map on which player is located.
     */
    private GameMap map;
    /** Player`s inventory.
     */
    private Inventory inventory;
    /** Drill entity.
     */
    private DrillRadius drillRadius;

    private boolean isInputRestricted = false;

    //TODO: мб можно как-то убрать глобальные переменные и засунуть их в метод
    private boolean isTimerRunning = false;  // Flag to track whether the timer is already running
    private float breakStateTime = 0;
    /** To hold the reference of the scheduled task.
     */
    private Task scheduledTask;
    /** Position to play the breaking animation.
     */
    private Vector2 cursorPos = new Vector2();
    /** Flag to track whether the player is moving.
     */
    boolean isMoving;
    /** Position to play the breaking animation.
     */
    private Vector2 cursorPosForAnimation = new Vector2();;
    /** Player`s animation state time.
     */
    private float stateTime;
    /** Player`s horizontal velocity.
     */
    private float velocityX = 0;
    /** Player`s frames for animations of left walking.
     */
    private Animation<TextureRegion> walkLeftAnimation;
    /** Player`s frames for animations of right walking.
     */
    private Animation<TextureRegion> walkRightAnimation;
    /** Player`s frame of idle state.
     */
    private TextureRegion idleTexture;
    /** Player`s current frame.
     */
    private TextureRegion currentFrame;
    /** Player`s current animation.
     */
    private Animation<TextureRegion> currentAnimation;
    /** Player`s oxygen amount.
     * Increasing while standing near working oxygen station.
     * Increasing while having working mobile oxygen generator in inventory.
     * Decreasing in other cases.
     */
    private float oxygen = 100;
    /** Player`s health amount.
     * Decreasing while oxygen amount is less than 0.
     * Increasing while oxygen is increasing.
     */
    private float health = 100;
    /** Renderer to dim screen.
     */
    private ShapeRenderer shapeRenderer;

    /**
     * Constructs a new Player object with the specified initial position, map, and inventory.
     *
     * @param x         the initial x-coordinate of the player
     * @param y         the initial y-coordinate of the player
     * @param map       the game map where the player is located
     * @param inventory the inventory of the player
     */
    public Player(float x, float y, GameMap map, Inventory inventory) {
        this.map = map;
        this.inventory = inventory;

        shapeRenderer = new ShapeRenderer();
        idleTexture = new TextureRegion(new Texture(Gdx.files.internal("assets/animations/player/player.png")));
        drillRadius = new DrillRadius();

        setPosition(x, y);
        setSize(idleTexture.getRegionWidth()/3.5f, idleTexture.getRegionHeight()/3.5f);
        initializePlayerAnimations();
    }

    /**
     * Initializes the player's animations
     *
     * @author Yehor Nesterenko
     */
    public void initializePlayerAnimations() {
        // Load each frame individually
        TextureRegion[] walkLeftFrames = new TextureRegion[3];

        TextureRegion[] walkRightFrames = new TextureRegion[3];


        fillFrames(walkLeftFrames, "assets/animations/player/walkLeft");
        fillFrames(walkRightFrames, "assets/animations/player/walkRight");

        // Create animations
        walkLeftAnimation = new Animation<>(0.1f, walkLeftFrames); // 0.1f is the frame duration
        walkRightAnimation = new Animation<>(0.1f, walkRightFrames);

        currentFrame = idleTexture;
    }

    /**
     * Fills the specified array of frames with the textures from the specified path.
     *
     * @param frames the array of frames to fill
     * @param path   the path to the textures
     *
     * @author Yehor Nesterenko
     */
    public void fillFrames(TextureRegion[] frames, String path) {
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new TextureRegion(new Texture(Gdx.files.internal(path + (i + 1) + ".png")));
        }
    }

    /**
     * Handles all player activity, input, movements, animations.
     *
     * @param delta the time in seconds since the last update
     */
    @Override
    public void act(float delta) {
        super.act(delta);

        stateTime += delta;
        updateAnimation();
        handleInput(delta);
        applyGravity(delta);
        applyOxygen();
        updatePosition(delta);
        updateBreakStateTime(delta);
    }

    /**
     * Applies the oxygen logic to the player.
     *
     * If the player is near the oxygen station, the oxygen level increases.
     * If the player has a working oxygen mobile generator in the inventory, the oxygen level increases.
     * If the player has no oxygen, the health level decreases.
     *
     * @author Mykola Isaiev
     */
    private void applyOxygen() {
        for (int distance : getDistances()) {
            if (distance < 500) {
                addOxygen();

                return;
            }
        }

        ArrayList<OxygenMobile> modules = inventory.getItemsOfClass(OxygenMobile.class);

        for(OxygenMobile module : modules){
            if(module.hasFuel() && !module.isWrapped()){
                module.useFuel(0.5f);

                addOxygen();

                return;
            }
        }

        if (oxygen - 0.015f < 0) {
            oxygen = 0;

            if (health - 0.15f < 0) {
                health = 0;
                GameMap.getPlayer().getMap().clearStage();
            } else {
                health -= 0.15f;
            }
        } else {
            oxygen -= 0.015f;
        }
    }

    /**
     * Adds oxygen to the player.
     *
     * @author Mykola Isaiev
     */
    public void addOxygen() {
        if (oxygen + 0.15f > 100) {
            oxygen = 100;
        } else {
            oxygen += 0.15f;
        }

        if (health + 0.1f > 100) {
            health = 100;
        } else {
            health += 0.1f;
        }
    }

    /**
     * Returns the distances between the player and the oxygen stations.
     *
     * @return the distances between the player and the oxygen stations
     *
     * @author Mykola Isaiev
     */
    private ArrayList<Integer> getDistances () {
        ArrayList<Integer> distances = new ArrayList<>();
        ArrayList<Vector2> oxygenSources = map.getOxygenStationsCoordinates();

        for (Vector2 oxygenSource : oxygenSources) {
            distances.add((int) Math.sqrt(Math.pow(oxygenSource.x - getX(), 2) + Math.pow(oxygenSource.y - getY(), 2)));
        }

        return distances;
    }

    /**
     * Updates the time spent in the breaking state.
     *
     * @param delta the time in seconds since the last update
     *
     * @author Yehor Nesterenko
     */
    public void updateBreakStateTime(float delta){
        if (isTimerRunning){
            breakStateTime += delta;
            if (getMap().getBreakAnimation().isAnimationFinished(breakStateTime)) {
                isTimerRunning = false; // Reset the animation
                breakStateTime = 0;
            }
        }
    }

    /**
     * Handles the player's input.
     *
     * Open/close inventory if 'I' is pressed and player doesn`t interact with gui.
     * Enter/leave drill mode on 'E' if player doesn`t interact with gui.
     * Move player left/right if 'A'/'D' is pressed.
     * Jump if 'SPACE' is pressed and player is on the ground.
     * Increase player's speed if 'SHIFT' is pressed and player is moving.
     * Close GUI if 'ESC' is pressed.
     * Zoom in/out if '+'/'-' is pressed.
     *
     * @param delta the time in seconds since the last update
     *
     * @author Mykola Isaiev
     */
    private void handleInput(float delta) {
        if(!isInputRestricted) {
            velocityX = 0;


            if (Gdx.input.isKeyJustPressed(Input.Keys.I) && !map.isPlaceableItemExists() && !GameMap.getPlayer().getMap().getActionsPopUpList().isVisible()) {
                drillMode = false;
                if (inventory.isVisible()) {
                    GameMap.getPlayer().getMap().getActionsPopUpList().dispose();
                    inventory.setVisible(false);
                } else {
                    inventory.changePositionByCoordinates(getX() - 200, getY() - 200);
                    inventory.setVisible(true);
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && GameMap.getPlayer().getMap().getActionsPopUpList().isVisible()) { // TODO: добавить все клавишы которые будут активны в инвентаре чтобы закрывать попап
                GameMap.getPlayer().getMap().getActionsPopUpList().dispose();
            }

            if (inventory.isVisible() || GameMap.getPlayer().getMap().getActionsPopUpList().isVisible()) {

            } else {
                isMoving = false;
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    velocityX = -SPEED;
//                moveX(-SPEED * delta);
                    isMoving = true;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    velocityX = SPEED;
//                moveX(SPEED * delta);
                    isMoving = true;
                }


                // Apply the calculated velocity to the player's position
                moveX(velocityX * delta);

                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    if(grounded){
                        if (stamina >= 10) {
                            stamina -= 10;
                            jump();
                            isMoving = true;
                        }
                    }else{
                        if(getInventory().containsItem("Jetpack")){
                            handleJetpackLogic();
                        }
                    }
                }


                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && isMoving) {
                    if (stamina > 0) {
                        SPEED = 300;
                        stamina--;
                    } else {
                        SPEED = 150;
                    }
                } else {
                    SPEED = 150;
                    if (stamina < 100 && !isMoving && grounded) {
                        stamina++;
                    }
                }

                // If the player is moving and a breaking task is scheduled, reset it
                if (isMoving && isTimerRunning) {
                    resetBreakingTask();
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
                    if (cameraZoom != 180) cameraZoom += 10;
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
                    if (cameraZoom != 30) cameraZoom -= 10;
                }

                if (drillMode) {
                    float distance = (float) Math.sqrt(Math.pow(cursorPos.x - getX(), 2) + Math.pow(cursorPos.y - getY(), 2));

                    drillRadius.setTooFar(distance > 400 ||
                            drillRadius.intersects(new Vector2(getX(), getY()), getWidth(), getHeight()));

                    drillRadius.setPosition(cursorPos.x - drillRadius.getWidth() / 2, cursorPos.y - drillRadius.getHeight() / 2);
                }

                // Check if the left mouse button is released
                if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    if (isTimerRunning && scheduledTask != null) {
                        scheduledTask.cancel();  // Cancel the ongoing task
                        breakStateTime = 0;      // Reset the break animation time
                        isTimerRunning = false;  // Mark timer as not running
                    }
                }

                processPlacing();
                handleBreakingInput();
                updateAnimation();

                if (Gdx.input.isKeyJustPressed(Input.Keys.E) && !map.isPlaceableItemExists()) {
                    drillMode = !drillMode;
                }
            }
        }
    }

    public void setInputRestricted(boolean isInputRestricted) {
        this.isInputRestricted = isInputRestricted;
    }

    private void handleJetpackLogic(){
        ArrayList<Jetpack> jetpacks = inventory.getItemsOfClass(Jetpack.class);
        for(Jetpack jetpack : jetpacks){
            if(jetpack.hasFuel()){
                jetpack.useFuel(5);
                velocityY = JETPACK_VELOCITY;
            }
        }
    }


    private void handleBreakingInput() {
        Vector2 newCursorPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());

        if (drillMode && Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !drillRadius.isTooFar() && grounded){
            double interactableTilesAmount = map.getInteractableTilesAmount(new Vector2(drillRadius.getX(), drillRadius.getY()), (int) drillRadius.getWidth(), (int) drillRadius.getHeight());
            if(interactableTilesAmount > 0) {
                if (!cursorPosForAnimation.epsilonEquals(newCursorPos, 5.0f) ) {
                    resetBreakingTask();
                    cursorPosForAnimation.set(newCursorPos);
                }

                    if (!isTimerRunning) {
                        isTimerRunning = true;
                        scheduledTask = Timer.schedule(new Task() {
                            @Override
                            public void run() {
                                processBreaking(newCursorPos);
                            }
                        }, 0.65f);
                    }

            }

        }
    }

    /**
     * Updates the player's animation.
     *
     * @author Yehor Nesterenko
     */
    private void updateAnimation() {
        // Determine the correct animation based on movement and state
       if (velocityX < 0) {
            currentAnimation = walkLeftAnimation;
        } else if (velocityX > 0) {
            currentAnimation = walkRightAnimation;
        } else {
            currentAnimation = null;  // No animation if not moving
            currentFrame = idleTexture;  // Use static idle image
        }

        // Update the animation state time and get current frame
        if (currentAnimation != null) {
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        }
    }

    /**
     * Resets the breaking task.
     *
     * @author Yehor Nesterenko
     */
    private void resetBreakingTask() {
        if (scheduledTask != null && isTimerRunning){
            scheduledTask.cancel();  // Cancel only if the task is running
        }
        breakStateTime = 0;
        isTimerRunning = false;
    }

    /**
     * Processes the breaking of the tiles.
     *
     * @param cursorPosition the position of the cursor
     *
     * @author Yehor Nesterenko
     */
    private void processBreaking(Vector2 cursorPosition) {

        if (drillRadius.isTooFar() || !Gdx.input.isButtonPressed(Input.Buttons.LEFT) || !cursorPosForAnimation.epsilonEquals(cursorPosition, 5.0f)  ) {
            resetBreakingTask();
            return;
        }

        HashMap<TileType, Integer> extractableResources = map.checkHowManyExtractableResourcesInArea(new Vector2(drillRadius.getX(), drillRadius.getY()), (int) drillRadius.getWidth(), (int) drillRadius.getHeight());

        for (Map.Entry<TileType, Integer> entry : extractableResources.entrySet()) {
            TileType tileType = entry.getKey();
            Integer count = entry.getValue();
            for (int i = 0; i < count; i++) {
                Item item = new RawResource(tileType.getName(), 1, "assets/items/resources/rawResources/" + tileType.getName() + ".png",
                        tileType, ItemSize.SMALL, getMap().getDragAndDrop());
                inventory.addItem(item);
            }
        }
        addToStorageIfPossible();

        resetBreakingTask();

    }

    /**
     * Processes the placing of the tiles.
     *
     * @author Yehor Nesterenko
     */
    private void processPlacing() {
        if (drillMode && Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !drillRadius.isTooFar()) {
            reduceStorageIfPossible();
        }
    }

    /**
     * Reduces the storage if possible.
     *
     * @author Yehor Nesterenko
     */
    private void reduceStorageIfPossible() {
        ArrayList<SoilStorage> items = inventory.getItemsOfClass(SoilStorage.class);
        if (items.isEmpty()) {
            return;
        }

        int totalTilesAvailable = getCurrentAmountOfStorage();

        TileType typeToPlace = TileType.getTileTypeById(27);
        Vector2 placePosition = new Vector2(drillRadius.getX(), drillRadius.getY());
        int areaWidth = (int) drillRadius.getWidth();
        int areaHeight = (int) drillRadius.getHeight();

        int amountToRemove = getMap().setTilesInAreaWithoutOverlay(placePosition, areaWidth, areaHeight, typeToPlace, totalTilesAvailable);

        if (amountToRemove == 0) {
            return;
        }

        for (Item item : items) {

                SmallSoilStorage storage = (SmallSoilStorage) item;
                if (storage.getCurrentAmount() > 0) {
                    int availableToRemove = Math.min(storage.getCurrentAmount(), amountToRemove);
                    storage.setCurrentAmount(storage.getCurrentAmount() - availableToRemove);
                    amountToRemove -= availableToRemove;

                    if (amountToRemove <= 0) {
                        break;
                    }
                }

        }
    }

    /**
     * Returns the current amount of storage.
     *
     * @return the current amount of storage
     *
     * @author Yehor Nesterenko
     */
    private int getCurrentAmountOfStorage(){
        ArrayList<SoilStorage> items = inventory.getItemsOfClass(SoilStorage.class);
        int currentAmount = 0;
        for (Item item : items) {

                SmallSoilStorage storage = (SmallSoilStorage) item;
                currentAmount += storage.getCurrentAmount();

        }
        return currentAmount;
    }

    /**
     * Adds to the storage if possible.
     *
     * @author Yehor Nesterenko
     */
    private void addToStorageIfPossible(){
        ArrayList<SoilStorage> items = inventory.getItemsOfClass(SoilStorage.class);
        TileType typeToPlace = TileType.getTileTypeById(38);
        Vector2 placePosition = new Vector2(drillRadius.getX(), drillRadius.getY());
        int totalAmountToDistribute = getMap().setTilesWithOverlay(placePosition, (int) drillRadius.getWidth(), (int) drillRadius.getHeight(), typeToPlace);
        if (items.isEmpty()) {
            return;
        }

        if (totalAmountToDistribute == 0) {
            return;
        }

        for (Item item : items) {

                SmallSoilStorage storage = (SmallSoilStorage) item;
                if (storage.getCurrentAmount() < storage.getCapacity()) {
                    int freeSpace = storage.getCapacity() - storage.getCurrentAmount();
                    int toStore = Math.min(freeSpace, totalAmountToDistribute);
                    storage.setCurrentAmount(storage.getCurrentAmount() + toStore);
                    totalAmountToDistribute -= toStore;

                    if (totalAmountToDistribute <= 0) {
                        break;
                    }
                }

        }
    }


    /**
     * Moves the player horizontally if not colliding with tiles.
     *
     * @param amount the amount to move the player horizontally
     *
     * @author Mykola Isaiev
     */
    private void moveX(float amount) {
        float newX = getX() + amount;
        if (!map.doesRectCollideWithMap(newX, getY(), (int) getWidth(), (int) getHeight())) {
            setX(newX);
        }
    }

    /**
     * Makes the player jump.
     *
     * @author Mykola Isaiev
     */
    private void jump() {
        velocityY = JUMP_VELOCITY;
        grounded = false;
    }

    /**
     * Applies gravity to the player.
     *
     * @param delta the time in seconds since the last update
     *
     * @author Mykola Isaiev
     */
    private void applyGravity(float delta) {
        velocityY += GRAVITY * delta;
    }

    /**
     * Updates the player's vertical position.
     *
     * @param delta the time in seconds since the last update
     *
     * @author Mykola Isaiev
     */
    private void updatePosition(float delta) {
        float newY = getY() + velocityY * delta;
        if (!map.doesRectCollideWithMap(getX(), newY, (int) getWidth(), (int) getHeight())) {
            setY(newY);
        } else {
            grounded = true;
            velocityY = 0;
        }
    }

    /**
     * Returns the player's inventory.
     *
     * @return the player's inventory
     *
     * @author Mykola Isaiev
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Returns the player's map.
     *
     * @return the player's map
     *
     * @author Mykola Isaiev
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Returns the player's state being in drill mode.
     *
     * @return the player's state being in drill mode
     *
     * @uthor Mykola Isaiev
     */
    public boolean isDrillMode() {
        return drillMode;
    }

    /**
     * Updates the cursor position based on the input from the user.
     * The position is unprojected using the provided viewport to convert screen coordinates to world coordinates.
     *
     * @param viewport the viewport used to unproject the screen coordinates
     *
     * @author Danylo Kost
     */
    public void updateCursorPosition(FitViewport viewport){
        Vector3 vec=new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
        viewport.unproject(vec);
        cursorPos.x = vec.x;
        cursorPos.y = vec.y;
    }

    /**
     * Returns the player's camera zoom value.
     *
     * @return the player's camera zoom value
     *
     * @uthor Danylo Kost
     */
    public int getCamZoom(){
        return cameraZoom;
    }

    /**
     * Draws the player on the screen.
     * If the player is in drill mode, the drill radius is also drawn.
     * If the player is in the breaking state, the breaking animation is drawn.
     * The oxygen level is drawn.
     * Dim screen if health is less than 100.
     *
     * @param batch       the batch to draw the player
     * @param parentAlpha the parent's alpha
     *
     * @author Mykola Isaiev
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (drillMode) {
            drillRadius.draw(batch, parentAlpha);
        }

        if (isTimerRunning && !drillRadius.isTooFar()){
            TextureRegion currentFrame = getMap().getBreakAnimation().getKeyFrame(breakStateTime);
            batch.draw(currentFrame, drillRadius.getX(), drillRadius.getY(), drillRadius.getWidth(), drillRadius.getHeight());
        }

        if(isMoving) {
            batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        } else {
            batch.draw(idleTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }

        float alpha = (100 - health) / 100;
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (health != 100) {
            shapeRenderer.setColor(new Color(0, 0, 0, alpha));
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        }
        shapeRenderer.setColor(new Color(0.031f, 0.757f, 0.961f, 1));
        shapeRenderer.rect(0, 0, 0.45f * oxygen, 10);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }
}