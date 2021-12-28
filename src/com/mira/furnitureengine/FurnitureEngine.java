package com.mira.furnitureengine;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.mira.furnitureengine.utils.UpdateChecker;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import com.mira.furnitureengine.commands.*;
import com.mira.furnitureengine.listeners.*;

import org.bukkit.ChatColor;

import com.mira.furnitureengine.Metrics;

@SuppressWarnings("unused")
public final class FurnitureEngine extends JavaPlugin {
	// WorldGuard Support
	public WorldGuardPlugin wg;
	
	// Update Checker
	public boolean versionChecked = false;
	public String versionOld = "";
	public String versionNew = "";
	
	public void onEnable() {	
		getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Furniture Engine enabled!");
		
		loadConfig();
		
		wg = getWorldGuard();
		
		// default
		getCommand("furnitureengine").setExecutor(new CoreCommand());
		 getCommand("furnitureengine").setTabCompleter(new CommandTabCompleter());
		  
		  // Event Handlers
		 new RightClick(this);
		 new FurniturePlace(this);
		 new FurnitureBreak(this);
		 
		 int pluginId = 13146;
	      Metrics metrics = new Metrics(this, pluginId);
		
		// Update Checker
	      new UpdateChecker(this, 97134).getVersion(version -> {
	          if (this.getDescription().getVersion().equals(version)) {
	              // Nothing happens;
	          } else {
	        	  versionChecked = true;
	        	  versionOld=this.getDescription().getVersion();
	        	  versionNew=version;
	        	  
	              getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "A new version is available: " + ChatColor.RED + "["+ versionOld + "]" + ChatColor.GRAY + " -> " + ChatColor.GOLD + "[" + versionNew + "]");
	              getLogger().info(ChatColor.AQUA + "https://www.spigotmc.org/resources/furnitureengine-1-16-1-17.97134/");
	          }
	      });

	}
	
	public void onDisable() {
		 getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Furniture Engine disabled!");
	 }
	  
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	private WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	    	getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "WorldGuard not found. Skipping!");
	        return null; // Maybe you want throw an exception instead
	    }

	    return (WorldGuardPlugin) plugin;
	}
}
