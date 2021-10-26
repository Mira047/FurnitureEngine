package me.mira.furnitureengine.eventManager;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.gsit.api.GSitAPI;
import me.gsit.objects.Seat;
import me.mira.furnitureengine.Main;
import me.mira.furnitureengine.events.FurnitureInteractEvent;

public class RightClickManager implements Listener {
	Main main = Main.getPlugin(Main.class);
	Seat seat;
	GSitAPI gsitapi;
	
	
	public RightClickManager(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
	}
	
	// Manages Block Interaction
	@EventHandler
	public void onBlockInteract(PlayerInteractEvent e) {
		Player player = (Player) e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK&&!player.isSneaking()) {
			Block clicked = e.getClickedBlock();
			Location blockLocation = clicked.getLocation();
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
		            					FurnitureInteractEvent event = new FurnitureInteractEvent(player, clicked.getLocation());
	                					Bukkit.getServer().getPluginManager().callEvent(event);
	                					if(!event.isCancelled()) {
		            					// Sitting
		            					if(main.getConfig().getBoolean("Furniture." + key+".chair.enabled")==true) {
		            						GSitAPI gsitapi = new GSitAPI();
		            						Double yoffset = main.getConfig().getDouble("Furniture." + key+".chair.yoffset");
		            						Location SeatLocation = new Location(clicked.getWorld(), clicked.getX(),clicked.getY(),clicked.getZ());
		            						gsitapi.setPlayerSeat(player, SeatLocation, 0, 0.7+yoffset, 0, 0, SeatLocation, true, false);
		            						return;
		            					}
		            					// Commands Executer
		            					for(int i=0;i<main.getConfig().getStringList("Furniture." + key + ".commands").size();i++){
		            						if(main.getConfig().getStringList("Furniture." + key + ".commands").size()>0) {
		            							String executable = main.getConfig().getStringList("Furniture." + key + ".commands").get(i);
		            							executable = executable.replace("<player>", player.getName());
		            							Boolean test = player.isOp();
		            							if(executable.startsWith("[op]")) {
		            								player.setOp(true);
		            								try {
		            									player.performCommand(executable.substring(4));
		            								} finally {
		            									if(test) {
		            										player.setOp(false);
		            									}
		            								}
		            							} else if(executable.startsWith("[c]")) {
		            								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), executable.substring(3));
		            							} else
		            							player.performCommand(executable);
		            						}
		            					}
		            				} else return;
		            				}
	                    		}
	            				
	            				return;
	            			});
	                    }
	                }
	            }
			}
		}
		return;
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent ev) {
		Player player = (Player) ev.getPlayer();
		Entity e = ev.getRightClicked();
		if(e instanceof ItemFrame&&!player.isSneaking()) {
			ItemFrame frame = (ItemFrame) e;
            if(frame.getItem().getType()==Material.OAK_PLANKS) {
            	ItemMeta meta = frame.getItem().getItemMeta();
            	main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
        				if(meta.getCustomModelData()==main.getConfig().getInt("Furniture." + key+".custommodeldata")) {
        					FurnitureInteractEvent event = new FurnitureInteractEvent(player, frame.getLocation().getBlock().getLocation());
        					Bukkit.getServer().getPluginManager().callEvent(event);
        					if(!event.isCancelled()) {
        					// Sitting
        					if(main.getConfig().getBoolean("Furniture." + key+".chair.enabled")==true) {
        						GSitAPI gsitapi = new GSitAPI();
        						Double yoffset = main.getConfig().getDouble("Furniture." + key+".chair.yoffset");
        						Location SeatLocation = new Location(frame.getLocation().getBlock().getLocation().getWorld(), frame.getLocation().getBlock().getLocation().getX(),frame.getLocation().getBlock().getLocation().getY()-1,frame.getLocation().getBlock().getLocation().getZ());
        						gsitapi.setPlayerSeat(player, SeatLocation, 0, 0.7+yoffset, 0, 0, SeatLocation, true, false);
        						return;
        					}
        					// Commands Executer
        					for(int i=0;i<main.getConfig().getStringList("Furniture." + key + ".commands").size();i++){
        						if(main.getConfig().getStringList("Furniture." + key + ".commands").size()>0) {
        							String executable = main.getConfig().getStringList("Furniture." + key + ".commands").get(i);
        							executable = executable.replace("<player>", player.getName());
        							Boolean test = player.isOp();
        							if(executable.startsWith("[op]")) {
        								player.setOp(true);
        								try {
        									player.performCommand(executable.substring(4));
        								} finally {
        									if(test) {
        										player.setOp(false);
        									}
        								}
        							} else if(executable.startsWith("[c]")) {
        								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), executable.substring(3));
        							} else
        							player.performCommand(executable);
        						}
        					}
        				} else return;
        				}
            		
    				
    				return;
    			});
		}
		}
	}
}
