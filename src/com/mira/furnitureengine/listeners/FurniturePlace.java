package com.mira.furnitureengine.listeners;


import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.World;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.api.events.FurniturePlaceEvent;
import com.plotsquared.core.plot.Plot;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import ru.beykerykt.minecraft.lightapi.common.LightAPI;

import com.mira.furnitureengine.utils.*;


public class FurniturePlace implements Listener {
	// TODO: New Placing System
	FurnitureEngine main = FurnitureEngine.getPlugin(FurnitureEngine.class);
    ItemStack itemFrameItem;
    public String id;

    public FurniturePlace(FurnitureEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, (Plugin) plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block blockPlaced = e.getBlockPlaced();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            item = player.getInventory().getItemInOffHand();
        }
        ItemMeta meta = item.getItemMeta();
        // Checking if placed block is custom furniture
        if (main.wg != null) {
            Location location = new Location(blockPlaced.getWorld(), blockPlaced.getX(), blockPlaced.getY(), blockPlaced.getZ());
            @SuppressWarnings("static-access")
            LocalPlayer localPlayer = main.wg.inst().wrapPlayer(player);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            if (item.getType() == Material.OAK_PLANKS && (player.hasPermission("furnitureengine.blockplace") || main.getConfig().getBoolean("Options.check-place-permissions") == false) && (query.testBuild(BukkitAdapter.adapt(location), localPlayer, Flags.BLOCK_PLACE) || player.hasPermission("furnitureengine.admin"))) {
            	if(Bukkit.getServer().getPluginManager().getPlugin("PlotSquared")!=null) {
            		com.plotsquared.core.location.Location loc = com.plotsquared.core.location.Location.at(blockPlaced.getLocation().getWorld().getName(), blockPlaced.getLocation().getBlockX(), blockPlaced.getLocation().getBlockY(), blockPlaced.getLocation().getBlockZ());
            		
            		Plot plot = Plot.getPlot(loc);
            		
            		for(String text: main.getConfig().getStringList("plot-worlds")) {
            			if(text.equals(blockPlaced.getLocation().getWorld().getName())) {
            				if (plot == null) {
            		            e.setCancelled(true);
            		            if(player.hasPermission("plots.admin.build.road")) e.setCancelled(false);
            		        }
            				if (!plot.isAdded(player.getUniqueId())) {
            					e.setCancelled(true);
            					if(player.hasPermission("plots.admin.build.unowned")) e.setCancelled(false);
            		        }
            			}
            		}
            		
            	}
            	if(!e.isCancelled()) PlaceBlock(meta, blockPlaced, player, e);
            } else if (item.getType() == Material.OAK_PLANKS && !(player.hasPermission("furnitureengine.blockplace") && main.getConfig().getBoolean("Options.check-place-permissions") == true)) {
                if (item.getItemMeta().hasCustomModelData()) {
                    e.setCancelled(true);
                }
            }
        } else
        if (item.getType() == Material.OAK_PLANKS && (player.hasPermission("furnitureengine.blockplace") || main.getConfig().getBoolean("Options.check-place-permissions") == false)) {
        	if(Bukkit.getServer().getPluginManager().getPlugin("PlotSquared")!=null) {
        		com.plotsquared.core.location.Location loc = com.plotsquared.core.location.Location.at(blockPlaced.getLocation().getWorld().getName(), blockPlaced.getLocation().getBlockX(), blockPlaced.getLocation().getBlockY(), blockPlaced.getLocation().getBlockZ());
        		
        		Plot plot = Plot.getPlot(loc);
        		
        		for(String text: main.getConfig().getStringList("plot-worlds")) {
        			if(text.equals(blockPlaced.getLocation().getWorld().getName())) {
        				if (plot == null) {
        		            e.setCancelled(true);
        		        }
        				if (!plot.isAdded(player.getUniqueId())) {
        					e.setCancelled(true);
        		        }
        			}
        		}
        		
        	}
        	if(!e.isCancelled()) PlaceBlock(meta, blockPlaced, player, e);
        } else if (item.getType() == Material.OAK_PLANKS && !(player.hasPermission("furnitureengine.blockplace") && main.getConfig().getBoolean("Options.check-place-permissions") == true)) {
            if (item.getItemMeta().hasCustomModelData()) {
                e.setCancelled(true);
            }
            return;
        }
    }

    private void PlaceBlock(ItemMeta meta, Block blockPlaced, Player player, BlockPlaceEvent e) {
        main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
            String test = main.getConfig().getString("Furniture." + key + ".display");
           
            if (meta.getDisplayName().equalsIgnoreCase(ItemUtils.translateHexColorCodes("&#","",ChatColor.translateAlternateColorCodes('&', test)))) {

                FurniturePlaceEvent event = new FurniturePlaceEvent(player, blockPlaced.getLocation());
                if(!Condition.checkForCondition(player, "-OnBlockPlace", key)) event.setCancelled(true);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    if (blockPlaced.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR || blockPlaced.getLocation().add(0, 1, 0).getBlock().getType() == Material.VOID_AIR || blockPlaced.getLocation().add(0, 1, 0).getBlock().getType() == Material.CAVE_AIR || blockPlaced.getLocation().add(0, 1, 0).getBlock().getType() == Material.TORCH || blockPlaced.getLocation().add(0, 1, 0).getBlock().getType() == Material.SOUL_TORCH) {
                    	// 1x1x1 Placing
                    	if(main.getConfig().getInt("Furniture." + key + ".width")==1&&main.getConfig().getInt("Furniture." + key + ".length")==1&&main.getConfig().getInt("Furniture." + key + ".height")==1) {
                    		blockPlaced.setType(Material.BARRIER);

                            // Placing item frame on top of block
                            World world = blockPlaced.getWorld();
                            ItemFrame frame = (ItemFrame) world.spawn(blockPlaced.getLocation().add(0, 1, 0), ItemFrame.class);
                            
                            frame.setInvulnerable(true);
                            frame.setFixed(true);
                            frame.setVisible(false);
                            frame.setItem(ItemUtils.setFrameItem(key));
                            frame.setFacingDirection(BlockFace.UP);
                            
                            if(main.getServer().getPluginManager().getPlugin("LightAPI")!=null) {
                            	LightAPI.get().setLightLevel(blockPlaced.getLocation().getWorld().getName(), blockPlaced.getLocation().getBlockX(), blockPlaced.getLocation().getBlockY(),blockPlaced.getLocation().getBlockZ(), main.getConfig().getInt("Furniture." + key + ".light-level"));
                            }
                            // Decides what direction the item frame item should be facing
                            float y = player.getLocation().getYaw();
                            if (y < 0) y += 360;
                            y %= 360;
                            int i = (int)((y + 8) / 22.5);

                            if (main.getConfig().getBoolean("Furniture." + key + ".full-rotate") == true) {
                                // 8 Side Rotation
                                // West
                                if (i == 15 || i == 0 || i == 1 || i == 16) frame.setRotation(Rotation.FLIPPED);
                                // North-West
                                if (i == 2) frame.setRotation(Rotation.FLIPPED_45);
                                // North
                                if (i == 3 || i == 4 || i == 5) frame.setRotation(Rotation.COUNTER_CLOCKWISE);
                                // North-East
                                if (i == 6) frame.setRotation(Rotation.COUNTER_CLOCKWISE_45);
                                // South-East
                                if (i == 10) frame.setRotation(Rotation.CLOCKWISE_45);
                                // South
                                if (i == 11 || i == 12 || i == 13) frame.setRotation(Rotation.CLOCKWISE);
                                // South-West
                                if (i == 14) frame.setRotation(Rotation.CLOCKWISE_135);
                                // East
                                if (i == 7 || i == 8 || i == 9) frame.setRotation(Rotation.NONE);


                            } else {
                                // 4 Side Rotation
                                if (i == 6 || i == 7 || i == 8 || i == 9) frame.setRotation(Rotation.NONE);
                                if (i == 10 || i == 11 || i == 12 || i == 13 || i == 14) frame.setRotation(Rotation.CLOCKWISE);
                                if (i == 2 || i == 3 || i == 4 || i == 5) frame.setRotation(Rotation.COUNTER_CLOCKWISE);
                                if (i == 15 || i == 16 || i == 0 || i == 1) frame.setRotation(Rotation.FLIPPED);
                            }
                            if(e.isCancelled()) frame.remove();
                            ListenerUtils.executeCommand("block-place", player, key);
                    	} else if(main.getConfig().getInt("Furniture." + key + ".height")==0) { // 0x0x0 Placing
                    		List < Entity > nearbyEntites = (List < Entity > ) blockPlaced.getWorld().getNearbyEntities(blockPlaced.getLocation().add(0, 1, 0), 2 ,2, 2);
                    		for (Entity nearbyEntity: nearbyEntites) {
                                if (nearbyEntity instanceof ItemFrame) {
                                	ItemFrame frame = (ItemFrame) nearbyEntity;

                                	if (frame.getItem().getType() == Material.OAK_PLANKS) {
                                        main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(id -> {
                                            if (main.getConfig().getInt("Furniture." + id + ".custommodeldata") == frame.getItem().getItemMeta().getCustomModelData()) {
                                                if(main.getConfig().getInt("Furniture." + id + ".height")==0&&frame.getLocation().getBlock().getLocation().getY() == blockPlaced.getLocation().getY() && frame.getLocation().getBlock().getLocation().getX() == blockPlaced.getLocation().getX() && frame.getLocation().getBlock().getLocation().getZ() == blockPlaced.getLocation().getZ()) {
                                                	e.setCancelled(true);   
                                                }
                                                return;
                                            }
                                        });
                                    }
                                }
                    		}
                    		if(!e.isCancelled()) {
                    			blockPlaced.setType(Material.AIR);
                        		World world = blockPlaced.getWorld();
                                ItemFrame frame = (ItemFrame) world.spawn(blockPlaced.getLocation(), ItemFrame.class);
                                frame.setInvulnerable(true);
                                frame.setFixed(true);
                                frame.setVisible(false);
                                frame.setItem(ItemUtils.setFrameItem(key));
                                frame.setFacingDirection(BlockFace.UP);

                                // Decides what direction the item frame item should be facing
                                float y = player.getLocation().getYaw();
                                if (y < 0) y += 360;
                                y %= 360;
                                int i = (int)((y + 8) / 22.5);

                                if (main.getConfig().getBoolean("Furniture." + key + ".full-rotate") == true) {
                                    // 8 Side Rotation
                                    // West
                                    if (i == 15 || i == 0 || i == 1 || i == 16) frame.setRotation(Rotation.FLIPPED);
                                    // North-West
                                    if (i == 2) frame.setRotation(Rotation.FLIPPED_45);
                                    // North
                                    if (i == 3 || i == 4 || i == 5) frame.setRotation(Rotation.COUNTER_CLOCKWISE);
                                    // North-East
                                    if (i == 6) frame.setRotation(Rotation.COUNTER_CLOCKWISE_45);
                                    // South-East
                                    if (i == 10) frame.setRotation(Rotation.CLOCKWISE_45);
                                    // South
                                    if (i == 11 || i == 12 || i == 13) frame.setRotation(Rotation.CLOCKWISE);
                                    // South-West
                                    if (i == 14) frame.setRotation(Rotation.CLOCKWISE_135);
                                    // East
                                    if (i == 7 || i == 8 || i == 9) frame.setRotation(Rotation.NONE);


                                } else {
                                    // 4 Side Rotation
                                    if (i == 6 || i == 7 || i == 8 || i == 9) frame.setRotation(Rotation.NONE);
                                    if (i == 10 || i == 11 || i == 12 || i == 13 || i == 14) frame.setRotation(Rotation.CLOCKWISE);
                                    if (i == 2 || i == 3 || i == 4 || i == 5) frame.setRotation(Rotation.COUNTER_CLOCKWISE);
                                    if (i == 15 || i == 16 || i == 0 || i == 1) frame.setRotation(Rotation.FLIPPED);
                                }
                                if(e.isCancelled()) frame.remove();
                                ListenerUtils.executeCommand("block-place", player, key);
                    		}
                    		
                    	}

                    } else {
                        e.setCancelled(true);
                    }
                } else e.setCancelled(true);
                return;
            }
        });
    }
}