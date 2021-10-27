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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.gsit.api.GSitAPI;
import me.gsit.objects.Seat;
import me.mira.furnitureengine.Main;
import me.mira.furnitureengine.Util;
import me.mira.furnitureengine.events.FurnitureInteractEvent;
import net.md_5.bungee.api.ChatColor;

public class RightClickManager implements Listener {
	static Main main = Main.getPlugin(Main.class);
	Seat seat;
	GSitAPI gsitapi;
	
	
	public RightClickManager(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
	}
	
	// Manages Block Interaction
	@EventHandler
	public void onBlockInteract(PlayerInteractEvent e) {
		if(e.getHand().toString().equals("OFF_HAND")) {
			e.setCancelled(true);
			return;
		}
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
		            					executeAction(player, frame, key);
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
        					executeAction(player, frame, key);
        				}
            		
    				
    				return;
    			});
            }
		}
	}
	
	public static void executeAction(Player player, ItemFrame frame, String key) {
		FurnitureInteractEvent event = new FurnitureInteractEvent(player, frame.getLocation().getBlock().getLocation());
		Bukkit.getServer().getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
		// Sitting
		if(main.getConfig().getBoolean("Furniture." + key+".chair.enabled")==true) {
			if(Bukkit.getServer().getPluginManager().getPlugin("GSit")!=null) {
				GSitAPI gsitapi = new GSitAPI();
				Double yoffset = main.getConfig().getDouble("Furniture." + key+".chair.yoffset");
				Location SeatLocation = new Location(frame.getLocation().getBlock().getLocation().getWorld(), frame.getLocation().getBlock().getLocation().getX(),frame.getLocation().getBlock().getLocation().getY()-1,frame.getLocation().getBlock().getLocation().getZ());
				if(gsitapi.getSeats(SeatLocation.getBlock()).size()==0&&gsitapi.getPlayerToggleState(player)==true) {
					gsitapi.setPlayerSeat(player, SeatLocation, 0, 0.7+yoffset, 0, 0, SeatLocation, true, false);
				}
			} else Bukkit.getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "GSit not installed! Sitting failed!");
			return;
		}
		// Commands Executer
		Util.executeCommand("right-click", player, key);
		} else return;
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
			System.out.println("Player joined");
			System.out.println(player.isOp());
			System.out.println(main.getConfig().getBoolean("Options.check-for-updates"));
			System.out.println(main.updateTest);
		if(player.isOp()&&main.getConfig().getBoolean("Options.check-for-updates")&&main.updateTest) {
			player.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "A new version is available: " + ChatColor.RED + "["+ main.version1 + "]" + ChatColor.GRAY + " -> " + ChatColor.GOLD + "[" + main.version2 + "]");
			player.sendMessage(ChatColor.AQUA + "https://www.spigotmc.org/resources/furnitureengine-1-16-1-17.97134/");
		}
	}
	
}
