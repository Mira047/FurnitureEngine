package com.mira.furnitureengine.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mira.furnitureengine.FurnitureEngine;

import net.md_5.bungee.api.ChatColor;

import java.lang.StringBuilder;

public class ItemUtils {
	private static Pattern gradient = Pattern.compile("<#[0-9a-fA-F]{6}>[^<]*</#[0-9a-fA-F]{6}>");
	public static String id;
	public static boolean test;
	static FurnitureEngine main = FurnitureEngine.getPlugin(FurnitureEngine.class);
	
	static ItemStack setItemMaterial(String id, int amount) {
		ItemStack item = new ItemStack(Material.AIR ,amount);
		if(main.getConfig().getString("Furniture." + id + ".item")!=null) {
			item.setType(Material.matchMaterial(main.getConfig().getString("Furniture." + id + ".item")));
		} else {
			// sets material to legacy material in case it fails
			item.setType(Material.OAK_PLANKS);
		}
		return item;
	}
	
	public static void giveItem(Player player,String id,int amount, Location loc) {
		if(loc==null) {
			// if no location is provided it will default to player location
			loc = player.getLocation();
		}

		ItemStack item = setHandItem(id, amount);
		
		if(player==null) {
			dropItem(item, loc);
			return;
		}
		if(!player.getInventory().addItem(item).isEmpty()) {
			dropItem(item, loc);
		}
		
		return;
	}
	
	public static void dropItem(ItemStack item, Location loc) {
		loc.getWorld().dropItem(loc, item);
		return;
	}
	
	public static ItemStack setHandItem(String id, int amount) {
		
		// Creates item
		ItemStack item = setItemMaterial(id, amount);
		// Sets item meta (display, lore, model data)
		ItemMeta meta = item.getItemMeta();
				
		// Display Name
		String display = main.getConfig().getString("Furniture." + id + ".display");
		display = setGradient(translateHexColorCodes("&#","",ChatColor.translateAlternateColorCodes('&', display)));
		meta.setDisplayName(display);
				
		// Custom Model Data
		meta.setCustomModelData(main.getConfig().getInt("Furniture." + id + ".custommodeldata"));
				
		// Lore (Optional check)
				
		if(!main.getConfig().getStringList("Furniture." + id + ".lore").isEmpty()) {
			List<String> loresList = new ArrayList<String>();
			for(String text : main.getConfig().getStringList("messages")) {
				loresList.add(setGradient(translateHexColorCodes("&#","",ChatColor.translateAlternateColorCodes('&', text))));
			}
			meta.setLore(loresList);
		}
				
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack setFrameItemByDisplay(String display) {
		
		main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
			if(main.getConfig().getString("Furniture." + key + ".display").equals(display)){
				id=key;
		}
		});
		if(id==null) return null;
		
		id = null;
		return setFrameItem(id);
	}
	
	public static ItemStack setFrameItem(String id) {
		ItemStack item = setItemMaterial(id, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(main.getConfig().getInt("Furniture." + id + ".custommodeldata"));
		item.setItemMeta(meta);
		return item;
	}
	
	public static boolean checkForFurniture(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
			if(main.getConfig().getString("Furniture." + key + ".display").equals(meta.getDisplayName())){
				test=true;
		}
		});
		if(test) {
			test = false;
			return true;
		}
		return false;
	}
	
	// colors
	
	public final static char COLOR_CHAR = ChatColor.COLOR_CHAR;
	
	public static String translateHexColorCodes(String startTag, String endTag, String message)
    {
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
                    );
        }
        return matcher.appendTail(buffer).toString();
    }
	
	// gradient
	
	//<#RRGGBB>Text</#RRGGBB>
   
    public static String setGradient(String s) {
    	String text = s;
    	Matcher m = gradient.matcher(text);
    	while(m.find()) {
    		String format = m.group();
    		TextColor start = new TextColor(format.substring(2, 8));
    		String message = format.substring(9, format.length() - 10);
    		TextColor end = new TextColor(format.substring(format.length() - 7, format.length() - 1));
    		String applied = asGradient(start, message, end);
    		text = text.replace(format, applied);
    		
    	}
    	return translateHexColorCodes("#", "", text);
    }
	
    private static String asGradient(TextColor start, String text, TextColor end) {
    	StringBuilder sb = new StringBuilder();
    	int length = text.length();  
    	String[] textArray = text.split("\\s+");
    	Float f = 0f;
    	int i = 1;
    	for(String word : textArray) {
    		f = (float) (end.red - start.red);
    		int red = Math.round((start.red + f / (length - 1) * i));
    		f = (float) (end.green - start.green);
    		int green = Math.round((start.green + f / (length - 1) * i));
    		f = (float) (end.blue - start.blue);
    		int blue = Math.round((start.blue + f / (length - 1) * i));
    		sb.append("#" + toHexString(red, green, blue) + word);
    		i++;
    	}
    	return sb.toString();
    }
    
    private static String toHexString(int red, int green, int blue) {
    	String s = Integer.toHexString((red >> 16) + (green >> 8) + blue);
    	while (s.length() < 6) s = "0$s";
    	return s;
    }
}
