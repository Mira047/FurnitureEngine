package com.mira.furnitureengine.furniture;

import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.furniture.core.Furniture;
import com.mira.furnitureengine.furniture.core.SubModel;

import java.util.ArrayList;
import java.util.List;

public class FurnitureManager {
    private static FurnitureManager instance;

    private static final FurnitureEngine plugin = FurnitureEngine.instance;

    private final List<Furniture> furniture = new ArrayList<>();

    public FurnitureManager() {
        instance = this;

        loadFurniture(true);
    }

    public static FurnitureManager getInstance() {
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
        loadFurniture(false);
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
}
