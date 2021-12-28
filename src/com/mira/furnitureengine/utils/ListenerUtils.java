package com.mira.furnitureengine.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;

import com.mira.furnitureengine.FurnitureEngine;

public class ListenerUtils {
	static FurnitureEngine main = FurnitureEngine.getPlugin(FurnitureEngine.class);
	
	public static void executeCommand(String mode, Player player, String key, @Nullable Location loc) {
		for(int i=0;i<main.getConfig().getStringList("Furniture." + key + ".commands." + mode).size();i++){
			if(main.getConfig().getStringList("Furniture." + key + ".commands." + mode).size()>0) {
				String executable = main.getConfig().getStringList("Furniture." + key + ".commands." + mode).get(i);
				executable = executable.replace("<player>", player.getName());
				
				executable = executable.replace("<location>", String.valueOf(loc.getX()) + " " + String.valueOf(loc.getY()-1) + " " + String.valueOf(loc.getZ()));
				System.out.println(String.valueOf(loc.getX()) + " " + String.valueOf(loc.getY()) + " " + String.valueOf(loc.getZ()));
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
