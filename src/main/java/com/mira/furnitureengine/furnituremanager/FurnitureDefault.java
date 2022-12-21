package com.mira.furnitureengine.furnituremanager;

import com.mira.furnitureengine.furnituremanager.furnituretypes.Furniture;
import com.mira.furnitureengine.utils.ItemUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;

import java.util.HashMap;
import java.util.List;

public class FurnitureDefault implements Furniture {
    final Material itemType = Material.OAK_PLANKS;

    final String id;
    final String displayName;

    final int width,length,height;

    final int blockCustomModelData;
    final int itemCustomModelData;

    final boolean chairEnabled;
    final double chairYOffset;

    final boolean fullRotate;

    HashMap<String, List<String>> commands = new HashMap<>();

    // Misc variables
    int lightLevel;


    public FurnitureDefault(Material itemType, String id, String displayName, int width, int length, int height, int blockCustomModelData, int itemCustomModelData, boolean chairEnabled, double chairYOffset, boolean fullRotate, int lightLevel, HashMap<String, List<String>> commands) {
        this.id = id;
        this.displayName = displayName;
        this.width = width;
        this.length = length;
        this.height = height;
        this.blockCustomModelData = blockCustomModelData;
        this.itemCustomModelData = itemCustomModelData;
        this.chairEnabled = chairEnabled;
        this.chairYOffset = chairYOffset;
        this.fullRotate = fullRotate;
        this.lightLevel = lightLevel;
        this.commands = commands;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getBlockCustomModelData() {
        return blockCustomModelData;
    }

    @Override
    public int getItemCustomModelData() {
        return itemCustomModelData;
    }

    @Override
    public boolean isChairEnabled() {
        return chairEnabled;
    }

    @Override
    public double getChairYOffset() {
        return chairYOffset;
    }

    @Override
    public boolean isFullRotateEnabled() {
        return fullRotate;
    }

    @Override
    public int getLightLevel() {
        return lightLevel;
    }

    @Override
    public HashMap<String, List<String>> getCommands() {
        return commands;
    }

    @Override
    public void setLightLevel(int lightLevel) {
        this.lightLevel = lightLevel;
    }

    @Override
    public void place(Block block, Rotation rotation) {
        if(height==1){
            // Normal Furniture
            // Checks if block above furniture is valid
            if(!block.getRelative(0,1,0).getType().isSolid()) {
                block.setType(Material.BARRIER);

                // Places item frame (entity) on top of the block
                World world = block.getWorld();
                ItemFrame frame = world.spawn(block.getLocation().add(0,1,0), ItemFrame.class);

                frame.setInvulnerable(true);
                frame.setFixed(true);
                frame.setVisible(false);
                frame.setFacingDirection(BlockFace.UP);

                frame.setRotation(rotation);

                // Sets item frame to the correct item
                frame.setItem(ItemUtils.setFrameItem(itemType, blockCustomModelData));
            }
        } else if(height==0){
            // Props
            try {
                throw new Exception("Props have not yet been implemented.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy(Block block) {
        if(height==1){
            // Normal Furniture

            List<Entity> nearbyEntites = (List <Entity>) block.getWorld().getNearbyEntities(block.getLocation().add(0, 1, 0), 0.13, 0.2, 0.13);

            for(Entity entity : nearbyEntites){
                if(entity instanceof ItemFrame frame){
                    if (frame.getLocation().getBlock().getLocation().add(0,-1,0).equals(block.getLocation())) {
                        frame.remove();

                        block.breakNaturally();
                    }
                }
            }
        }
    }
}
