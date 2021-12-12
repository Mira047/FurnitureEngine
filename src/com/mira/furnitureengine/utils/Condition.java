package com.mira.furnitureengine.utils;

import java.util.List;

import org.bukkit.entity.Player;

import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.conditions.*;

public class Condition {
	static FurnitureEngine main = FurnitureEngine.getPlugin(FurnitureEngine.class);
	public static boolean isOk;
	
	public static boolean checkForCondition(Player player, String type, String id) {
		isOk = true;
		List<String> condList = main.getConfig().getStringList("Furniture." + id + ".conditions");
		
		for(String text : condList) {
			if(text.endsWith(type)) {
				if(isOk) {
					String prefix = text.substring(0, text.lastIndexOf("{"));
					String input = getText(text);
					switch(prefix.toLowerCase()) {
						case "biome" -> {
							isOk = ConditionBiome.check(isOk, player, input);
						}
						case "world" -> {
							isOk = ConditionWorld.check(isOk, player, input);
						}
						case "permission" -> {
							isOk = ConditionPermission.check(isOk, player, input);
						}
						case "weather" -> {
							isOk = ConditionWeather.check(isOk, player, input);
						}
						case "time" -> {
							isOk = ConditionTime.check(isOk, player, input);
						}
					}
				} else return false;
			}
		}
		return isOk;	
	}
	
	public static String getText(String input) {
		int s = input.indexOf("{") + 1;
		int e = input.lastIndexOf("}");
		if (s > 0 && e > s) {
		    return(input.substring(s, e));
		}
		return null;
	}
}
