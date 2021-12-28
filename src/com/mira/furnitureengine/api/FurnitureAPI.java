package com.mira.furnitureengine.api;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import com.mira.furnitureengine.utils.*;

import com.mira.furnitureengine.listeners.*;
import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.api.events.FurnitureBreakEvent;
import com.mira.furnitureengine.api.events.FurniturePlaceEvent;

public class FurnitureAPI {
	static FurnitureEngine main = FurnitureEngine.getPlugin(FurnitureEngine.class);
	
	// Places furniture at location.
	public void PlaceFurniture(String id, Location blockLocation, Rotation rotation) {
		Block block = blockLocation.getBlock();
		FurniturePlaceEvent event = new FurniturePlaceEvent(null, blockLocation);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
		if(block.getLocation().add(0,1,0).getBlock().getType()==Material.AIR) {
			block.setType(Material.BARRIER);
			
			// Placing item frame on top of block
			World world = block.getWorld();
			ItemFrame frame = (ItemFrame) world.spawn(block.getLocation().add(0, 1, 0),ItemFrame.class);
			frame.setInvulnerable(true);
			frame.setFixed(true);
			frame.setVisible(false);
			frame.setItem(ItemUtils.setFrameItem(id));
			frame.setFacingDirection(BlockFace.UP);
			
			// Rotation of item-frame
			frame.setRotation(rotation);
			return;
		}
	}
	}
	
	// Destroys furniture at location.
	public void BreakFurniture(Location blockLocation) {
		Block block = blockLocation.getBlock();
		FurnitureBreakEvent event = new FurnitureBreakEvent(null, blockLocation);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
		if(block.getType()==Material.BARRIER) {
			List<Entity> nearbyEntites = (List<Entity>) block.getWorld().getNearbyEntities(block.getLocation().add(0, 1, 0), 0.13, 0.2, 0.13);
			for (Entity nearbyEntity : nearbyEntites) {
                if (nearbyEntity instanceof ItemFrame) {
                    ItemFrame frame = (ItemFrame) nearbyEntity;
                    if(frame.getItem().getType()==Material.OAK_PLANKS) {
                    	main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
                    		if(main.getConfig().getInt("Furniture." + key + ".custommodeldata")==frame.getItem().getItemMeta().getCustomModelData()) {
                    			if(frame.getLocation().getBlock().getLocation().getY()-1==block.getLocation().getY()&&frame.getLocation().getBlock().getLocation().getX()==block.getLocation().getX()&&frame.getLocation().getBlock().getLocation().getZ()==block.getLocation().getZ()) {
                    				frame.remove();
                    				block.breakNaturally();
			                    	Location loc = block.getLocation();
			                    		
			                    	block.getWorld().playSound(loc, Sound.BLOCK_WOOD_BREAK, 3, 1);
			                    	if(event.isDroppingItems()) {
		                    			ItemUtils.giveItem(null, key, 1, loc);
		                    		}
		                    		
                    			}
	            				return;
                    		}
            			});
                    }
                }
			}
		}
		}
		return;
	}
	
	// Simulates a player interacting with furniture.
	public void SimulateInteraction(Player player, Location blockLocation) {
		Block clicked = blockLocation.getBlock();
		World world = clicked.getWorld();
		if(clicked.getType()==Material.BARRIER) {
			List<Entity> nearbyEntites = (List<Entity>) world.getNearbyEntities(blockLocation.add(0, 1, 0), 0.5, 0.5, 0.5);
			for (Entity nearbyEntity : nearbyEntites) {
                if (nearbyEntity instanceof ItemFrame) {
                    ItemFrame frame = (ItemFrame) nearbyEntity;
                    if(frame.getItem().getType()==Material.OAK_PLANKS) {
                    	ItemMeta meta = frame.getItem().getItemMeta();
                    	main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
                    		if(frame.getLocation().getBlock().getLocation().getY()-1==clicked.getLocation().getY()&&frame.getLocation().getBlock().getLocation().getX()==clicked.getLocation().getX()&&frame.getLocation().getBlock().getLocation().getZ()==clicked.getLocation().getZ()) {
	            				if(meta.getCustomModelData()==main.getConfig().getInt("Furniture." + key+".custommodeldata")) {
	            					RightClick.executeAction(player, frame, key, blockLocation);
	            				}
                    		}
            				
            				return;
            			});
                    }
                }
			}
		}
	}
	
	// I wanted to keep stuff simple so instead of having to access "Util" these things can be found in the FurnitureAPI
	public void GiveFurniture(String id,Player player, int amount) {
		ItemUtils.giveItem(player, id, amount, null);
		return;
	}
	
	// Checks if there is furniture at a location. Returns furniture id or null.
	public String CheckForFurniture(Location blockLocation) {
		return Utils.checkForFurniture(blockLocation);
	}
}

