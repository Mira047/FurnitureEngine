package me.mira.furnitureengine.commands;

import java.util.ArrayList;
import java.util.List;

import me.mira.furnitureengine.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CoreTabCompleter implements TabCompleter {
	Main main = Main.getPlugin(Main.class);
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		
		if(args.length == 1) {
			List<String> autoCompletion = new ArrayList<>();
			autoCompletion.add("reload");
			autoCompletion.add("give");
			
			return autoCompletion;
		}
		if(args.length == 2&&args[0].equals("give")) {
			List<String> autoCompletion = new ArrayList<>();
			
			main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
				autoCompletion.add(key);
			});
			
			return autoCompletion;
		}
		if(args.length ==3&&args[0].equals("give")) {
			List<String> autoCompletion = new ArrayList<>();
			for(int i=1;i<=99;i++) {
				autoCompletion.add(Integer.toString(i));
			}
			return autoCompletion;
		}
		return null;
	}
}
