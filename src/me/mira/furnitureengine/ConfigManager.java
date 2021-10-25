package me.mira.furnitureengine;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
	private static File file;
	private static FileConfiguration config;
	
	public static void setup(){
		file = new File(Bukkit.getServer().getPluginManager().getPlugin("FurnitureEngine").getDataFolder(), "config.yml");
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public static FileConfiguration get() {
		return config;
	}
	
	public static void Save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void Reload() {
		config = YamlConfiguration.loadConfiguration(file);
	}
}
