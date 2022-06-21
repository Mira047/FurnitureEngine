package com.mira.furnitureengine;

import com.mira.furnitureengine.commands.CommandTabCompleter;
import com.mira.furnitureengine.commands.MainCommand;
import com.mira.furnitureengine.integrations.WorldGuard;
import com.mira.furnitureengine.listeners.BlockPlaceListener;
import com.mira.furnitureengine.listeners.PlayerInteractListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class FurnitureEngine extends JavaPlugin {

    @Override
    public void onEnable() {
        loadConfig();

        // Plugin startup logic
        getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Furniture Engine enabled!");

        getCommand("furnitureengine").setExecutor(new MainCommand());
        getCommand("furnitureengine").setTabCompleter(new CommandTabCompleter());

        // Event Listeners instantiation goes here
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

        // WorldGuard integration
        Plugin worldGuard = getServer().getPluginManager().getPlugin("WorldGuard");
        WorldGuard.init(worldGuard);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Furniture Engine disabled!");
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
