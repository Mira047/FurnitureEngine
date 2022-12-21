package com.mira.furnitureengine.furnituremanager.furnituretypes;

import org.bukkit.Rotation;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.List;

public interface Furniture {
    /**
     * @return the id of the furniture
     */
    String getId();

    /**
     * @return the display name of the furniture
     */
    String getDisplayName();

    /**
     * @return the width of the furniture
     */
    int getWidth();

    /**
     * @return the length of the furniture
     */
    int getLength();

    /**
     * @return the height of the furniture
     */
    int getHeight();

    /**
     * @return the custom model data of the furniture block
     */
    int getBlockCustomModelData();

    /**
     * @return the custom model data of the furniture item
     */
    int getItemCustomModelData();

    /**
     * @return if the furniture is a chair
     */
    boolean isChairEnabled();

    /**
     * @return the y offset of the chair
     */
    double getChairYOffset();

    /**
     * @return if the furniture can be rotated in all directions
     */
    boolean isFullRotateEnabled();

    /**
     * @return the light level of the furniture
     */
    int getLightLevel();

    /**
     * Sets the light level of the furniture
     * @param lightLevel the light level
     */
    void setLightLevel(int lightLevel);

    /**
     * @return the commands of the furniture
     */
    HashMap<String, List<String>> getCommands();

    /**
     * Places the furniture
     * @param block the block to place the furniture on
     * @param rotation the rotation of the furniture
     */
    void place(Block block, Rotation rotation);

    /**
     * Destroys the furniture
     * @param block the block to destroy the furniture on
     */
    void destroy(Block block);
}
