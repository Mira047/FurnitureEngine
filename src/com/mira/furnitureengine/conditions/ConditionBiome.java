package com.mira.furnitureengine.conditions;

import org.bukkit.entity.Player;

public class ConditionBiome {
	public static boolean check(boolean org, Player player, String input) {
		input = input.replace("b=","");
		
		if(player.getLocation().getBlock().getBiome().toString().equals(input)) {
			if(!org) return false;
			return true;
		}
		return false;
	}
}
