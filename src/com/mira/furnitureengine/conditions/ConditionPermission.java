package com.mira.furnitureengine.conditions;

import org.bukkit.entity.Player;

public class ConditionPermission {
	public static boolean check(boolean org, Player player, String input) {
		input = input.replace("w=","");
		
		if(player.hasPermission(input)) {
			if(!org) return false;
			return true;
		}
		return false;
	}
}
