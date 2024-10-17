package com.mygdx.game.world;

import java.util.HashMap;

/**
 * Represents the types of tiles that can exist in the game.
 * This enum includes various types of grass, stone, ores, decorations, and sky.
 * Each tile type has an ID, a collidable property, a name, and an extractable property.
 *
 * @author Mykola Isaiev
 */
public enum TileType {

    //GRASS
    TOP_GRASS1(1,true, "Top Grass 1"),
    GRASS1(2,true, "Grass 1"),
    TOP_GRASS2(3,true, "Top Grass 2"),
    GRASS2(4,true, "Grass 2"),
    TOP_GRASS3(5,true, "Top Grass 3"),
    GRASS3(6,true, "Grass 3"),
    TOP_GRASS4(7,true, "Top Grass 4"),
    GRASS4(8,true, "Grass 4"),
    TOP_GRASS5(9,true, "Top Grass 5"),
    GRASS5(10,true, "Grass 5"),

    //STONE
    STONE(11,true, "Stone"),
    DEEP_STONE(24,true, "Deep Stone"),

    //ORES
    COPPER_SMALL(12,true, "Copper", true),
    COPPER_BIG(13,true, "Copper", true),

    COAL_SMALL(14,true, "Coal", true),
    COAL_BIG(15,true, "Coal", true),

    ALUMINIUM_SMALL(16,true, "Aluminium", true),
    ALUMINIUM_BIG(17,true, "Aluminium", true),

    RESIN_SMALL(18,true, "Resin", true),
    RESIN_BIG(19,true, "Resin", true),

    IRON_SMALL(20,true, "Iron", true),
    IRON_BIG(21,true, "Iron", true),

    COMPOUND_SMALL(22,true, "Compound", true),
    COMPOUND_BIG(23,true, "Compound", true),

    DEEP_RUBY_SMALL(25,true, "Ruby", true),
    DEEP_RUBY_BIG(26,true, "Ruby", true),

    //PLAYER`S SOIL
    PLAYER_SOIL(27,true, "Player Soil"),

    //DECORATIONS
    FLOWER1(28, true, "Flower2"),
    FLOWER2(29, true, "Flower2"),
    FLOWER3(30, true, "Flower3"),
    FLOWER4(31, true, "Flower4"),

    ROCK1(32, true, "Rock1"),
    ROCK2(33, true, "Rock2"),

    ORGANIC1(34, true, "Organic1"),
    ORGANIC2(35, true, "Organic2"),
    ORGANIC3(36, true, "Organic3"),
    ORGANIC4(37, true, "Organic2"),


    //SKY
    SKY(38, false, "Sky"),
    CLOUD(39, false, "Cloud");


    /**
     * The size of a tile.
     */
    public static final int TILE_SIZE = 16;

    /**
     * The ID of the tile type.
     */
    private int id;

    /**
     * Whether the tile type is collidable.
     */
    private boolean collidable;

    /**
     * The name of the tile type.
     */
    private String name;

    /**
     * Whether the tile type is extractable.
     */
    private boolean extractable;

    /**
     * Returns whether the tile type is extractable.
     *
     * @return True if the tile type is extractable, false otherwise.
     *
     * @author Mykola Isaiev
     */
    public boolean isExtractable() {
        return extractable;
    }

    /**
     * Constructor for creating a TileType instance.
     *
     * @param id The ID of the tile type.
     * @param collidable Whether the tile type is collidable.
     * @param name The name of the tile type.
     * @param extractable Whether the tile type is extractable.
     *
     * @author Mykola Isaiev
     */
    TileType(int id, boolean collidable, String name, boolean extractable){
        this.id = id;
        this.collidable = collidable;
        this.name = name;
        this.extractable = extractable;
    }

    /**
     * Constructor for creating a TileType instance.
     *
     * @param id The ID of the tile type.
     * @param collidable Whether the tile type is collidable.
     * @param name The name of the tile type.
     *
     * @author Mykola Isaiev
     */
    TileType(int id, boolean collidable, String name){
        this.id = id;
        this.collidable = collidable;
        this.name = name;
        extractable = false;
    }

    /**
     * Returns the ID of the tile type.
     *
     * @return The ID of the tile type.
     *
     * @author Mykola Isaiev
     */
    public int getId(){
        return id;
    }

    /**
     * Returns the name of the tile type.
     *
     * @return The name of the tile type.
     *
     * @author Mykola Isaiev
     */
    public String getName(){
        return name;
    }

    /**
     * Returns whether the tile type is collidable.
     *
     * @return True if the tile type is collidable, false otherwise.
     *
     * @author Mykola Isaiev
     */
    public boolean isCollidable() {
        return collidable;
    }

    /**
     * A map from tile type IDs to tile types.
     */
    private static HashMap<Integer, TileType> tileMap;

    /**
     * Initializes the tile map.
     */
    static{
        tileMap = new HashMap<>();
        for(TileType tileType : TileType.values()){
            tileMap.put(tileType.getId(),tileType);
        }
    }

    /**
     * Returns the tile type corresponding to the given ID.
     *
     * @param id The ID of the tile type.
     * @return The tile type corresponding to the given ID.
     *
     * @author Mykola Isaiev
     */
    public static TileType getTileTypeById(int id){
        return tileMap.get(id);
    }
}