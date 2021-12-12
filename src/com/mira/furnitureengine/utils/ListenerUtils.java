package com.mira.furnitureengine.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mira.furnitureengine.FurnitureEngine;

public class ListenerUtils {
	static FurnitureEngine main = FurnitureEngine.getPlugin(FurnitureEngine.class);
	
	public static void executeCommand(String mode, Player player, String key) {
		for(int i=0;i<main.getConfig().getStringList("Furniture." + key + ".commands." + mode).size();i++){
			if(main.getConfig().getStringList("Furniture." + key + ".commands." + mode).size()>0) {
				String executable = main.getConfig().getStringList("Furniture." + key + ".commands." + mode).get(i);
				executable = executable.replace("<player>", player.getName());
				Boolean test = player.isOp();
				if(Condition.checkForCondition(player, "-OnCommand[" + i+1 + "]", key)) {
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
		return;
	}
	

}