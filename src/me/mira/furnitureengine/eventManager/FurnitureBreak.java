package me.mira.furnitureengine.eventManager;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.protection.flags.*;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.sk89q.worldedit.bukkit.BukkitAdapter;

import me.mira.furnitureengine.Main;
import net.md_5.bungee.api.ChatColor;

public class FurnitureBreak implements Listener {
	Main main = Main.getPlugin(Main.class);
	public Location furnitureLocation;
	
	public FurnitureBreak(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
	}
	
	@EventHandler
	public void onBlockInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
			Player player = (Player) e.getPlayer();
			Block clicked = e.getClickedBlock();
			if(main.wg!=null) {
				Location location = new Location(clicked.getWorld(),clicked.getX(),clicked.getY(),clicked.getZ());
				LocalPlayer localPlayer = main.wg.inst().wrapPlayer(player);
				RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
				RegionQuery query = container.createQuery();
				if(clicked.getType()==Material.BARRIER&&(player.hasPermission("furnitureengine.blockbreak")||main.getConfig().getBoolean("Options.check-place-permissions")==false)&&(query.testState(BukkitAdapter.adapt(location), localPlayer, Flags.BUILD)||player.hasPermission("furnitureengine.admin"))) {	
					List<Entity> nearbyEntites = (List<Entity>) clicked.getWorld().getNearbyEntities(clicked.getLocation().add(0, 1, 0), 0.13, 0.2, 0.13);
					for (Entity nearbyEntity : nearbyEntites) {
		                if (nearbyEntity instanceof ItemFrame) {
		                    ItemFrame frame = (ItemFrame) nearbyEntity;
		                    if(frame.getItem().getType()==Material.OAK_PLANKS) {
		                    	main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
		                    		if(main.getConfig().getInt("Furniture." + key + ".custommodeldata")==frame.getItem().getItemMeta().getCustomModelData()) {
		                    			if(frame.getLocation().getBlock().getLocation().getY()-1==clicked.getLocation().getY()&&frame.getLocation().getBlock().getLocation().getX()==clicked.getLocation().getX()&&frame.getLocation().getBlock().getLocation().getZ()==clicked.getLocation().getZ()) {
		                    				frame.remove();
		                    				if(player.getGameMode()!=GameMode.CREATIVE) {
				                    			clicked.breakNaturally();
					                    		
					                    		Location loc = clicked.getLocation();
					                    		
					                    		player.playSound(loc, Sound.BLOCK_WOOD_BREAK, 3, 1);
				                    			dropItem(key,loc);
				                    		}
		                    			}

			            				
			            				return;
		                    		}
		            			});
		                    }
		                }
			

			
					}
					
				}
			} else
			if(clicked.getType()==Material.BARRIER&&(player.hasPermission("furnitureengine.blockbreak")||main.getConfig().getBoolean("Options.check-place-permissions")==false)) {	
				List<Entity> nearbyEntites = (List<Entity>) clicked.getWorld().getNearbyEntities(clicked.getLocation().add(0, 1, 0), 0.13, 0.2, 0.13);
				for (Entity nearbyEntity : nearbyEntites) {
	                if (nearbyEntity instanceof ItemFrame) {
	                    ItemFrame frame = (ItemFrame) nearbyEntity;
	                    if(frame.getItem().getType()==Material.OAK_PLANKS) {
	                    	main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
	                    		if(main.getConfig().getInt("Furniture." + key + ".custommodeldata")==frame.getItem().getItemMeta().getCustomModelData()) {
	                    			if(frame.getLocation().getBlock().getLocation().getY()-1==clicked.getLocation().getY()&&frame.getLocation().getBlock().getLocation().getX()==clicked.getLocation().getX()&&frame.getLocation().getBlock().getLocation().getZ()==clicked.getLocation().getZ()) {
	                    				frame.remove();
	                    				if(player.getGameMode()!=GameMode.CREATIVE) {
			                    			clicked.breakNaturally();
				                    		
				                    		Location loc = clicked.getLocation();
				                    		
				                    		player.playSound(loc, Sound.BLOCK_WOOD_BREAK, 3, 1);
			                    			dropItem(key,loc);
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
	
	public void dropItem(String id, Location loc) {
		ItemStack item = new ItemStack(Material.OAK_PLANKS ,1);
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(main.getConfig().getInt("Furniture." + id + ".custommodeldata"));
		String display = main.getConfig().getString("Furniture." + id + ".display");
		display = ChatColor.translateAlternateColorCodes('&', display);
		meta.setDisplayName(display);
		
		item.setItemMeta(meta);
		
		loc.getWorld().dropItem(loc, item);
		
		
	}

}
