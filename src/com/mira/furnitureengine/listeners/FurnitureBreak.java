package com.mira.furnitureengine.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;

import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import ru.beykerykt.minecraft.lightapi.common.LightAPI;

import com.sk89q.worldguard.protection.flags.*;
import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.api.events.FurnitureBreakEvent;
import com.mira.furnitureengine.api.events.PropPickUpEvent;
import com.mira.furnitureengine.utils.*;
import com.plotsquared.core.plot.Plot;
import com.sk89q.worldedit.bukkit.BukkitAdapter;


public class FurnitureBreak implements Listener {
	FurnitureEngine main = FurnitureEngine.getPlugin(FurnitureEngine.class);
    public Location furnitureLocation;
    public boolean breakTest;
    Location loc2;

    public FurnitureBreak(FurnitureEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, (Plugin) plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onBlockInteract(final PlayerInteractEvent e) {
    	
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player player = (Player) e.getPlayer();
            Block clicked = e.getClickedBlock();
            
            if (main.wg != null) {
                Location location = new Location(clicked.getWorld(), clicked.getX(), clicked.getY(), clicked.getZ());
                @SuppressWarnings("static-access")
                LocalPlayer localPlayer = main.wg.inst().wrapPlayer(player);
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionQuery query = container.createQuery();
                
                if (clicked.getType().equals(Material.BARRIER) && (player.hasPermission("furnitureengine.blockbreak") || main.getConfig().getBoolean("Options.check-place-permissions") == false) && (query.testState(BukkitAdapter.adapt(location), localPlayer, Flags.BLOCK_BREAK) || player.hasPermission("furnitureengine.admin"))) {
      
                    BreakBlock(clicked, player);
                }
            } else
            if (clicked.getType().equals(Material.BARRIER) && (player.hasPermission("furnitureengine.blockbreak") || main.getConfig().getBoolean("Options.check-place-permissions") == false)) {
                BreakBlock(clicked, player);
            }
        }
        return;
    }

    private void BreakBlock(Block clicked, Player player) {
        List < Entity > nearbyEntites = (List < Entity > ) clicked.getWorld().getNearbyEntities(clicked.getLocation().add(0, 1, 0), 0.13, 0.2, 0.13);
        for (Entity nearbyEntity: nearbyEntites) {
            if (nearbyEntity instanceof ItemFrame) {
                ItemFrame frame = (ItemFrame) nearbyEntity;
                if (frame.getItem().getType() == Material.OAK_PLANKS) {
                    main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
                        if (main.getConfig().getInt("Furniture." + key + ".custommodeldata") == frame.getItem().getItemMeta().getCustomModelData()) {
                            if (frame.getLocation().getBlock().getLocation().getY() - 1 == clicked.getLocation().getY() && frame.getLocation().getBlock().getLocation().getX() == clicked.getLocation().getX() && frame.getLocation().getBlock().getLocation().getZ() == clicked.getLocation().getZ()) {
                                FurnitureBreakEvent event = new FurnitureBreakEvent(player, clicked.getLocation());
                                Bukkit.getServer().getPluginManager().callEvent(event);
                                if(!Condition.checkForCondition(player, "-OnBlockBreak", key)) event.setCancelled(true);
                                if (!event.isCancelled()) {
                                	breakTest=false;
                                	if(Bukkit.getServer().getPluginManager().getPlugin("PlotSquared")!=null) {
                                		com.plotsquared.core.location.Location loc = com.plotsquared.core.location.Location.at(clicked.getLocation().getWorld().getName(), clicked.getLocation().getBlockX(), clicked.getLocation().getBlockY(), clicked.getLocation().getBlockZ());
                                		
                                		Plot plot = Plot.getPlot(loc);
                                		
                                		for(String text: main.getConfig().getStringList("plot-worlds")) {
                                			if(text.equals(clicked.getLocation().getWorld().getName())) {
                                				if (plot == null) {
                                					breakTest=true;
                                		            if(player.hasPermission("plots.admin.build.road")) breakTest=false;
                                		        }
                                				if (!plot.isAdded(player.getUniqueId())) {
                                					breakTest=true;
                                					if(player.hasPermission("plots.admin.build.unowned")) breakTest=false;
                                		        }
                                			}
                                		}
                                		
                                	}
                                	if(!breakTest) {
                                		frame.remove();
                                        if(main.getServer().getPluginManager().getPlugin("LightAPI")!=null) {
                                        	LightAPI.get().setLightLevel(clicked.getLocation().getWorld().getName(), clicked.getLocation().getBlockX(), clicked.getLocation().getBlockY(),clicked.getLocation().getBlockZ(), 0);
                                        }
                                        if (player.getGameMode() != GameMode.CREATIVE) {
                                            clicked.breakNaturally();

                                            Location loc = clicked.getLocation();

                                            player.playSound(loc, Sound.BLOCK_WOOD_BREAK, 3, 1);
                                            if(main.getConfig().getBoolean("Furniture." + key + ".cancel-item-drop")) event.dropItems(false);
                                            if (event.isDroppingItems()) {
                                          
                                                ItemUtils.giveItem(null, key, 1, loc);
                                            }
                                            ListenerUtils.executeCommand("block-break", player, key, clicked.getLocation());
                                        }
                                	}
                                }
                            }
                            return;
                        }
                    });
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityHit(PlayerInteractEntityEvent event) {
    	Player player = event.getPlayer();
    	Entity e = event.getRightClicked();
    	List < Entity > nearbyEntites = (List < Entity > ) e.getLocation().getBlock().getLocation().getWorld().getNearbyEntities(e.getLocation().getBlock().getLocation(), 0.13, 0.2, 0.13);
    	for (Entity nearbyEntity: nearbyEntites) {
    	if(nearbyEntity instanceof ItemFrame) {
    		ItemFrame frame = (ItemFrame) nearbyEntity;
    		if(player.isSneaking()) {
    			main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
                    if (main.getConfig().getInt("Furniture." + key + ".custommodeldata") == frame.getItem().getItemMeta().getCustomModelData()) {
                    	if(main.getConfig().getInt("Furniture." + key + ".height")==0) {
                    		if (main.wg != null) {
                                Location location = new Location(frame.getLocation().getBlock().getWorld(), frame.getLocation().getBlock().getX(), frame.getLocation().getBlock().getY(), frame.getLocation().getBlock().getZ());
                                @SuppressWarnings("static-access")
                                LocalPlayer localPlayer = main.wg.inst().wrapPlayer(player);
                                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                                RegionQuery query = container.createQuery();

                                if ((player.hasPermission("furnitureengine.blockbreak") || main.getConfig().getBoolean("Options.check-place-permissions") == false) && (query.testBuild(BukkitAdapter.adapt(location), localPlayer, Flags.BLOCK_BREAK) || player.hasPermission("furnitureengine.admin"))) {
                                	PickUpProp(frame, player, key);
                                }
                            } else
                            if ((player.hasPermission("furnitureengine.blockbreak") || main.getConfig().getBoolean("Options.check-place-permissions") == false)) {
                            	PickUpProp(frame, player, key);
                            	}
                    		}
                    	}
                	});		
    			}
    		}
    	}
    }
    
    public void PickUpProp(ItemFrame frame, Player player, String key) {
    	
    	PropPickUpEvent event = new PropPickUpEvent(player, frame.getLocation().getBlock().getLocation());
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
        	ItemUtils.giveItem(player, key, 1, frame.getLocation().getBlock().getLocation());
        	frame.remove();
        }
    	return;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPacketReceiving(final PacketEvent event) {
        event.setCancelled(false);
    }
    
}