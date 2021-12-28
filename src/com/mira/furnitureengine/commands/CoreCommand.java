package com.mira.furnitureengine.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.api.events.FurnitureBreakEvent;
import com.mira.furnitureengine.utils.*;

import net.md_5.bungee.api.ChatColor;

public class CoreCommand implements CommandExecutor {
	FurnitureEngine main = FurnitureEngine.getPlugin(FurnitureEngine.class);
  
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length==0) {
			sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Incorrect Command usage!");
			return false;
		}
		if(args[0].equals("reload")) {
			this.reloadCommand(sender);
			return true;
		}
		if(args[0].equals("give")) {
			if(args.length==1) {
				sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Incorrect Command usage!");
				return false;
			} else {
					main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
						if(args[2].startsWith(key)) {
							if(args.length==4&&Integer.parseInt(args[3])!=0) {
								ItemUtils.giveItem(Bukkit.getPlayer(args[1]),key,Integer.parseInt(args[3]),null);
							} else ItemUtils.giveItem(Bukkit.getPlayer(args[1]),key, 1, null);
						} 
					});
				
			}
			 
			return true;
		}
		if(args[0].equals("get")) {
			if(args.length==1) {
				sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Incorrect Command usage!");
				return false;
			} else {
				if(sender instanceof Player) {
					main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
						if(args[1].startsWith(key)) {
							if(args.length==3&&Integer.parseInt(args[2])!=0) {
								ItemUtils.giveItem((Player) sender,key,Integer.parseInt(args[2]),null);
							} else ItemUtils.giveItem((Player) sender,key, 1, null);
						} 
					});
				}
			}
			 
			return true;
		}
		if(args[0].equals("remove")) {
			if(sender instanceof Player p) {
				if(args.length<4) {
					sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Incorrect Command usage!");
					return false;
				} else {
						BreakFurniture(new Location(p.getLocation().getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])));
				}
				 
				return true;
			}
		}

		return false;
	}
	
	// Commands
	
	public boolean reloadCommand(CommandSender sender) {
		main.reloadConfig();
		sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Config reloaded!");
		return true;
	}
	
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
}
