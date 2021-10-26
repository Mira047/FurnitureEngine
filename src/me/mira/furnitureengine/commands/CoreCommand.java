package me.mira.furnitureengine.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.mira.furnitureengine.Util;

import me.mira.furnitureengine.Main;
import net.md_5.bungee.api.ChatColor;

public class CoreCommand implements CommandExecutor {
	Main main = Main.getPlugin(Main.class);
  
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
				if(sender instanceof Player) {
					Player player = (Player) sender;
					main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
						if(args[1].startsWith(key)) {
							if(args.length==3&&Integer.parseInt(args[2])!=0) {
								Util.giveItem(key,player,Integer.parseInt(args[2]));
							} else Util.giveItem(key,player, 1);
						} 
					});
				}
			}
			 
			return true;
		}

		return false;
	}
	
	// Commands
	
	public boolean reloadCommand(CommandSender sender) {
		main.reloadConfig();
		sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Config reloaded!");
		return true;
	}
}
