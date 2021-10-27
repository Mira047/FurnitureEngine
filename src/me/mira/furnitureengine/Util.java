package me.mira.furnitureengine;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Util {
	static Main main = Main.getPlugin(Main.class);
	public static String id;
	public static String furnitureTest;
	
	// Sets item. Used for item frames.
	
	public static ItemStack setItem(String displayName) {
		ItemStack item = new ItemStack(Material.OAK_PLANKS, 1);
		ItemMeta meta = item.getItemMeta();
		main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
			if(main.getConfig().getString("Furniture." + key + ".display").equals(displayName)){
				id=key;
		}
		});
		if(id==null) return null;
		
		meta.setCustomModelData(main.getConfig().getInt("Furniture." + id + ".custommodeldata"));
		
		item.setItemMeta(meta);
		
		id=null;
		return item;
	}
	
	// Sets item but uses id instead of Display Name
	public static ItemStack setItemWithId(String id) {
		ItemStack item = new ItemStack(Material.OAK_PLANKS, 1);
		ItemMeta meta = item.getItemMeta();
		if(id==null) return null;
		
		meta.setCustomModelData(main.getConfig().getInt("Furniture." + id + ".custommodeldata"));
		
		item.setItemMeta(meta);
		
		id=null;
		return item;
	}
	
	// Drops furniture item on ground.
	public static void dropItem(String id, Location location) {
		ItemStack item = new ItemStack(Material.OAK_PLANKS ,1);
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(main.getConfig().getInt("Furniture." + id + ".custommodeldata"));
		String display = main.getConfig().getString("Furniture." + id + ".display");
		display = ChatColor.translateAlternateColorCodes('&', display);
		meta.setDisplayName(display);
		
		item.setItemMeta(meta);
		
		location.getWorld().dropItem(location, item);
		
		return;
	}
	
	// Gives furniture item to a player.
	public static void giveItem(String id,Player player, int amount) {
		ItemStack item = new ItemStack(Material.OAK_PLANKS ,amount);
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(main.getConfig().getInt("Furniture." + id + ".custommodeldata"));
		String display = main.getConfig().getString("Furniture." + id + ".display");
		display = ChatColor.translateAlternateColorCodes('&', display);
		meta.setDisplayName(display);
		
		item.setItemMeta(meta);
		player.getInventory().addItem(item);
		
		return;
	}
	
	// Checks for furniture at a location
	
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
	
	public static void executeCommand(String mode, Player player, String key) {
		for(int i=0;i<main.getConfig().getStringList("Furniture." + key + ".commands." + mode).size();i++){
			if(main.getConfig().getStringList("Furniture." + key + ".commands." + mode).size()>0) {
				String executable = main.getConfig().getStringList("Furniture." + key + ".commands." + mode).get(i);
				executable = executable.replace("<player>", player.getName());
				Boolean test = player.isOp();
				if(executable.startsWith("[op]")) {
					player.setOp(true);
					try {
						// OP Command
						player.performCommand(executable.substring(4));
					} finally {
						if(!test) {
							player.setOp(false);
						}
					}
				} else if(executable.startsWith("[c]")) {
					// Console Command
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), executable.substring(3));
				} else {
					// Normal Command
					player.performCommand(executable);
				}
			}
		}
	}
	
}
