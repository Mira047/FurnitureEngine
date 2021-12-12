package com.mira.furnitureengine.utils;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;

import com.mira.furnitureengine.FurnitureEngine;

public class Utils {
	static FurnitureEngine main = FurnitureEngine.getPlugin(FurnitureEngine.class);
	static String furnitureTest = "";
	
	public static String checkForFurniture(Location location) {
		furnitureTest = null;
		Block block = location.getBlock();
		if(block.getType()==Material.BARRIER) {
			List<Entity> nearbyEntites = (List<Entity>) block.getWorld().getNearbyEntities(block.getLocation().add(0, 1, 0), 0.13, 0.2, 0.13);
			for (Entity nearbyEntity : nearbyEntites) {
                if (nearbyEntity instanceof ItemFrame) {
                    ItemFrame frame = (ItemFrame) nearbyEntity;
                    if(frame.getItem().getType()==Material.OAK_PLANKS) {
                    	main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
                    		if(main.getConfig().getInt("Furniture." + key + ".custommodeldata")==frame.getItem().getItemMeta().getCustomModelData()) {
                    			if(frame.getLocation().getBlock().getLocation().getY()-1==block.getLocation().getY()&&frame.getLocation().getBlock().getLocation().getX()==block.getLocation().getX()&&frame.getLocation().getBlock().getLocation().getZ()==block.getLocation().getZ()) {
                    				furnitureTest = key;
                    			}
	            				return;
                    		}
            			});
                    	if(furnitureTest!=null) {
                    		return furnitureTest;
                    	} else return null;
                    }
                }
			}
		}
		return null;
	}
}
