package me.mira.furnitureengine;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.mira.furnitureengine.commands.CoreCommand;
import me.mira.furnitureengine.commands.CoreTabCompleter;
import me.mira.furnitureengine.eventManager.RightClickManager;
import me.mira.furnitureengine.eventManager.FurnitureBreak;
import me.mira.furnitureengine.eventManager.FurniturePlace;
import net.md_5.bungee.api.ChatColor;

public final class Main extends JavaPlugin {
	public WorldGuardPlugin wg;


  public void onEnable() {	  
	  getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Furniture Engine enabled!");
	  
	  // config setup
	  
	  loadConfig();
	  
	  // Plugin Startup
	  
	  wg = getWorldGuard();
	  
	  getCommand("furnitureengine").setExecutor(new CoreCommand());
	  getCommand("furnitureengine").setTabCompleter(new CoreTabCompleter());
	  
	  // Event Handlers
	  new FurnitureBreak(this);
	  new RightClickManager(this);
	  new FurniturePlace(this);
	  
	  
  }
  
  public void onDisable() {
	  getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Furniture Engine disabled!");
  }
  
  public void loadConfig() {
	  getConfig().options().copyDefaults(true);
	  saveConfig();
  }
  
  // I'm Not even gonna lie I just copy pasted this from somewhere
  private WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	    	getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "WorldGuard not found. Skipping!");
	        return null; // Maybe you want throw an exception instead
	    }

	    return (WorldGuardPlugin) plugin;
	}
	  
  

}