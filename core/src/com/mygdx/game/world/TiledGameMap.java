package com.mygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a game map that is based on tiles.
 * This class extends the GameMap class and provides functionality for managing a tiled game map.
 * It includes methods for rendering the map, updating the map, and interacting with the tiles on the map.
 *
 * @author Mykola Isaiev
 * @author Danylo Kost
 * @author Anton Makasevych
 */
public class TiledGameMap extends GameMap {

    /**
     * The tiled map object that this game map is based on.
     */
    TiledMap tiledmap;

    /**
     * The renderer used to display the tiled map.
     */
    OrthogonalTiledMapRenderer tiledMapRenderer;

    /**
     * The camera used to view the map.
     */
    OrthographicCamera cam;

    /**
     * The viewport used to display the map.
     */
    ExtendViewport viewport;

    /**
     * Constructor for creating a TiledGameMap instance.
     * This constructor also sets up the camera, viewport, and renderer, and generates the terrain for the map.
     */
    public TiledGameMap(){
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);

        tiledmap = new TiledMap();
        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledmap = mapLoader.load("map/map.tmx");
        clearTiles();

        int mapWidth = 2001;
        int mapHeight = 251;
        Noisy voice = new Noisy(30, 30, .10f);
        float[][] heightmap = voice.generatePerlinNoise(mapWidth, mapHeight);
        generateTerrain(heightmap);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledmap, 1);

        player.setPosition(2500, findHighestPoint(2500) + 10);
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
    public void setTileByCoordinate(int layer, int col, int row, TileType type){
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tiledmap.getTileSets().getTile(type.getId()));
        ((TiledMapTileLayer) tiledmap.getLayers().get(layer)).setCell(col, row, cell);
    }

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
    public void setTileByLocation(int layer, float x, float y, TileType type){
        this.setTileByCoordinate(layer, (int)(x / TileType.TILE_SIZE), (int)(y / TileType.TILE_SIZE), type);
    }

    /**
     * Renders the map and all entities on it.
     *
     * @param batch The SpriteBatch used to render the map.
     *
     * @author Danylo Kost
     */
    @Override
    public void render( SpriteBatch batch) {
        //updating viewport for map rendering
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        tiledMapRenderer.setView((OrthographicCamera) viewport.getCamera());
        tiledMapRenderer.render();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        super.render(batch);
        batch.end();
    }

    /**
     * Resizes the viewport to the specified width and height.
     *
     * @param width The new width of the viewport.
     * @param height The new height of the viewport.
     *
     * @author Danylo Kost
     */
    @Override
    public void resize(int width, int height){

    }

    /**
     * Updates the map and all entities on it.
     *
     * @param delta The time since the last update.
     *
     * @author Danylo Kost
     */
    @Override
    public void update(float delta) {
        cam.zoom = player.getCamZoom()/100f;
        cam.position.set(player.getX(), player.getY(), 0);

        super.update(delta);
    }

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
    @Override
    public TileType getTileTypeByCoordinate(int layer, int col, int row) {
        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) tiledmap.getLayers().get(layer)).getCell(col,row);
        if(cell != null){
            TiledMapTile tile = cell.getTile();
            if(tile != null){
                int id = tile.getId();
                return TileType.getTileTypeById(id);
            }
        }
        return null;
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
    @Override
    public TileType getTileTypeByLocation(int layer, float x, float y){
        return this.getTileTypeByCoordinate(layer,(int)(x / TileType.TILE_SIZE), (int)(y/TileType.TILE_SIZE));
    }

    @Override
    public void dipsose() {
        tiledmap.dispose();
    }

    /**
     * Returns the width of the map in tiles.
     *
     * @return The width of the map in tiles.
     *
     * @author Mykola Isaiev
     */
    @Override
    public float getWidth() {
        return ((TiledMapTileLayer) tiledmap.getLayers().get(0)).getWidth();
    }

    /**
     * Returns the height of the map in tiles.
     *
     * @return The height of the map in tiles.
     *
     * @author Mykola Isaiev
     */
    @Override
    public float getHeight() {
        return ((TiledMapTileLayer) tiledmap.getLayers().get(0)).getHeight();
    }

    /**
     * Returns the number of layers in the map.
     *
     * @return The number of layers in the map.
     *
     * @author Mykola Isaiev
     */
    @Override
    public int getLayers() {
        return tiledmap.getLayers().getCount();
    }

    /**
     * Clears all tiles from the map.
     *
     * @author Anton Makasevych
     */
    public void clearTiles() {
        int numLayers = tiledmap.getLayers().getCount();
        for (int layerIndex = 0; layerIndex < numLayers; layerIndex++) {
            TiledMapTileLayer layer = (TiledMapTileLayer) tiledmap.getLayers().get(layerIndex);
            int layerWidth = layer.getWidth();
            int layerHeight = layer.getHeight();
            for (int col = 0; col < layerWidth; col++) {
                for (int row = 0; row < layerHeight; row++) {
                    layer.setCell(col, row, null);
                }
            }
        }
    }

    /**
     * Represents a set of values used for terrain generation.
     * This class includes frequency, amplitude, and flatAmplitude values.
     *
     * @author Mykola Isaiev
     */
    private class Values {
        /**
         * The frequency value used for terrain generation.
         */
        float frequency;

        /**
         * The amplitude value used for terrain generation.
         */
        float amplitude;

        /**
         * The flatAmplitude value used for terrain generation.
         */
        float flatAmplitude;

        /**
         * Constructor for creating a Values instance.
         *
         * @param frequency The frequency value used for terrain generation.
         * @param amplitude The amplitude value used for terrain generation.
         * @param flatAmplitude The flatAmplitude value used for terrain generation.
         *
         * @author Mykola Isaiev
         */
        public Values(float frequency, float amplitude, float flatAmplitude) {
            this.frequency = frequency;
            this.amplitude = amplitude;
            this.flatAmplitude = flatAmplitude;
        }

        /**
         * Returns the frequency value.
         *
         * @return The frequency value.
         *
         * @author Mykola Isaiev
         */
        public float getFrequency() {
            return frequency;
        }

        /**
         * Returns the amplitude value.
         *
         * @return The amplitude value.
         *
         * @author Mykola Isaiev
         */
        public float getAmplitude() {
            return amplitude;
        }

        /**
         * Returns the flatAmplitude value.
         *
         * @return The flatAmplitude value.
         *
         * @author Mykola Isaiev
         */
        public float getFlatAmplitude() {
            return flatAmplitude;
        }
    }

    /**
     * Generates the terrain for the map based on a heightmap.
     *
     * @param heightmap The heightmap to use for terrain generation.
     *
     * @author Anton Makasevych
     */
    private void  generateTerrain(float[][] heightmap) {
        Random random = new Random();
        int[] grassTiles = {1, 3, 5, 7, 9};
        // Smoother frequency for more natural curves
        float frequency = 0.0005f; // Reduced frequency for smoother hills 0.1f-0.005f
        float amplitude = 15.0f; // Amplitude to give hills noticeable height 10f-40f
        float flatAmplitude = 0.5f; // Reduced flat amplitude for less variation in flat areas
        float phaseShift = random.nextFloat() * 2 * (float) Math.PI; // Random phase for variation 0.001f-1f

        ArrayList<Values> values = new ArrayList<>();
        values.add(new Values(0.0005f, 15.0f, 0.5f)); // flat
        values.add(new Values(0.10105f, 50f, 0.5f)); // sharp mountains
        values.add(new Values(0.1005f, 15.0f, 0.5f)); // medium hills
        values.add(new Values(0.1f, 4.0f, 5f)); // little hills
        values.add(new Values(0.1005f, 40.0f, 0.5f)); // mountains
        values.add(new Values(0.0105f, 10.0f, 0.5f)); // flat

        float lastYOffset = 0;
        int randomIndex = random.nextInt(grassTiles.length);
        int terrainDepth = 5;

        for (int x = 0; x < heightmap.length; x++) {
            if (x % 100 == 0) {
                randomIndex = grassTiles[random.nextInt(grassTiles.length)];

                Values randomValues = values.get(random.nextInt(values.size()));
                frequency = randomValues.getFrequency();
                amplitude = randomValues.getAmplitude();
                flatAmplitude = randomValues.getFlatAmplitude();

                phaseShift = random.nextFloat() * 2 * (float) Math.PI;

                int min = 2;
                int max = 7;
                terrainDepth = random.nextInt(max - min + 1) + min;
            }

            float lerpFactor = 0.1f;
            boolean isHilly = random.nextDouble() < 0.75;
            float targetYOffset;
            if (isHilly) {
                targetYOffset = amplitude * (float) Math.sin(frequency * x + phaseShift);
            } else {
                targetYOffset = flatAmplitude * (random.nextFloat() - 0.5f);
            }
            float yOffset = lastYOffset + lerpFactor * (targetYOffset - lastYOffset);
            lastYOffset = yOffset;

            for (int y = 0; y < heightmap[x].length; y++) {

                int terrainHeight = (int) (170 + yOffset);

                TileType tileType = TileType.getTileTypeById(38);
                if (y > terrainHeight + 6 && random.nextDouble() < 0.005) {
                    tileType = TileType.getTileTypeById(39);
                }

                if (y < terrainHeight) {
                    tileType = TileType.getTileTypeById(randomIndex);

                    if (y < terrainHeight - 1){
                        tileType = TileType.getTileTypeById(randomIndex + 1);
                    }

                    if (y < terrainHeight - terrainDepth) {
                        tileType = TileType.STONE;

                        if (random.nextDouble() < 0.010) {
                            int min = 12;
                            int max = 23;
                            int randomOre = random.nextInt(max - min + 1) + min;
                            tileType = TileType.getTileTypeById(randomOre);
                        }
                    }

                    if (y < terrainHeight - 130){
                        tileType = TileType.getTileTypeById(24);

                        if (random.nextDouble() < 0.005) {
                            int min = 25;
                            int max = 26;
                            int randomOre = random.nextInt(max - min + 1) + min;
                            tileType = TileType.getTileTypeById(randomOre);
                        }
                    }

                }
                setTileByCoordinate(0, x, y, TileType.SKY);
                setTileByCoordinate(1, x, y, tileType);
            }
        }
    }
}