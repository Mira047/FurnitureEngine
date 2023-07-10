package com.mira.furnitureengine.furniture;

import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.furniture.core.Furniture;
import com.mira.furnitureengine.furniture.core.SubModel;
import com.mira.furnitureengine.furniture.functions.FunctionManager;
import com.mira.furnitureengine.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FurnitureManager {
    private static FurnitureManager instance;

    private static final FurnitureEngine plugin = FurnitureEngine.getInstance();

    private final List<Furniture> furniture = new ArrayList<>();

    private FurnitureManager() {
        loadFurniture(true);
    }

    public static FurnitureManager getInstance() {
        if(instance == null) {
            instance = new FurnitureManager();
        }
        return instance;
    }

    private void loadFurniture(boolean log) {
        furniture.clear();
        for(String furnitureName : plugin.getConfig().getConfigurationSection("Furniture").getKeys(false)) {
            try {
                Furniture furniture = new Furniture(furnitureName);

                this.furniture.add(furniture);

                if(log) plugin.getLogger().info("Loaded furniture: " + furniture.toString());
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Failed to load furniture: " + furnitureName);
            }
        }
    }

    public void reloadFurniture() {
        loadFurniture(true);
    }

    /**
     * Returns a specific furniture asset by its id
     * @param id The id of the furniture
     * @return The furniture asset
     */
    public Furniture getFurniture(String id) {
        for(Furniture furniture : this.furniture) {
            if(furniture.getId().equalsIgnoreCase(id)) return furniture;
        }
        return null;
    }

    /**
     * Returns a list of all furniture assets
     * @return The list of furniture assets
     */
    public List<Furniture> getFurniture() {
        return furniture;
    }

    /**
     * Returns a list of all furniture ids
     * @return The list of furniture ids
     */
    public List<String> getIds() {
        List<String> ids = new ArrayList<>();
        for(Furniture furniture : this.furniture) {
            ids.add(furniture.getId());
        }
        return ids;
    }

    /**
     * Checks if a block at a specific location is a furniture block. This also checks for submodels.
     * @param location The location of the block
     * @return The furniture asset, or null if it's not a furniture block
     */
    public Furniture isFurniture(Location location) {
        // Check if there is a barrier block at the location
        if (location.getBlock().getType() != Material.BARRIER) return null;

        // Get the middle of the block, so that it's as accurate as possible
        location = location.clone().add(0.5, 0, 0.5);

        // get all entities at the location, and check if one of them is an item frame
        Collection<Entity> entities = location.getWorld().getNearbyEntities(location, 0.2, 0.2, 0.2);

        for (Entity entity : entities) {
            if (entity.getType() != EntityType.ITEM_FRAME) continue;

            ItemFrame itemFrame = (ItemFrame) entity;

            // Check if the item is a furniture item
            for (Furniture furniture : this.furniture) {
                if (Utils.itemsMatch(itemFrame.getItem(), furniture.getBlockItem())) return furniture;

                // Additionally, check if any of the submodels match
                for (SubModel subModel : furniture.getSubModels()) {
                    if (Utils.itemsMatch(itemFrame.getItem(), furniture.generateSubModelItem(subModel))) return furniture;
                }
            }
        }

        return null;
    }
}
