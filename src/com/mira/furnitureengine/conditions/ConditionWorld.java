package com.mira.furnitureengine.conditions;

import org.bukkit.entity.Player;

public class ConditionWorld {
	public static boolean check(boolean org, Player player, String input) {
		input = input.replace("w=","");
		
		if(player.getLocation().getBlock().getWorld().getName().equals(input)) {
			if(!org) return false;
			return true;
		}
		return false;
	}
}
