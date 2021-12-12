package com.mira.furnitureengine.conditions;

import org.bukkit.entity.Player;

public class ConditionWeather {
	public static boolean check(boolean org, Player player, String input) {
		input = input.replace("w=","");
		boolean thunder = player.getLocation().getWorld().isThundering();
		boolean raining = player.getLocation().getWorld().hasStorm();
		switch(input) {
		case "raining" -> {
			if (raining)
				if(!org) return false;
				return true;
		}
		case "thundering" -> {
			if (thunder)
				if(!org) return false;
				return true;
		}
	}
		return false;
	}
}
