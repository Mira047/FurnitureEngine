package com.mira.furnitureengine;

import com.mira.furnitureengine.commands.CommandTabCompleter;
import com.mira.furnitureengine.commands.MainCommand;
import com.mira.furnitureengine.integrations.GSit;
import com.mira.furnitureengine.integrations.WorldGuard;
import com.mira.furnitureengine.listeners.BlockPlaceListener;
import com.mira.furnitureengine.listeners.PlayerInteractListener;
import com.mira.furnitureengine.utils.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class FurnitureEngine extends JavaPlugin {
    public static FurnitureEngine instance;

    @Override
    public void onEnable() {
        instance = this;

        if (this.getConfig().getBoolean("Options.check-for-updates")) {
            new UpdateChecker(97134).getVersion(version -> {
                if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                    this.getLogger().info("You are running the latest version of FurnitureEngine!");
                } else {
                    this.getLogger().info("There is a new update available for FurnitureEngine!" + " (Current version: " + this.getDescription().getVersion() + " New version: " + version + ")");
                }
            });
        }

        new Metrics(this, 13146);

        loadConfig();

        // Plugin startup logic
        getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Furniture Engine enabled!");

        org.bukkit.command.PluginCommand command = getCommand("furnitureengine");

        assert command != null;
        command.setExecutor(new MainCommand());
        command.setTabCompleter(new CommandTabCompleter());

        // Event Listeners instantiation goes here
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

        loadIntegrations();

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

    public void loadIntegrations() {
        /* WorldGuard integration */
        Plugin worldGuard = getServer().getPluginManager().getPlugin("WorldGuard");
        WorldGuard.init(worldGuard);

        if(worldGuard!=null) getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Found WorldGuard installed!");

        /* GSit integration */
        Plugin gSit = getServer().getPluginManager().getPlugin("GSit");
        GSit.init(gSit);

        if(gSit!=null) getLogger().info(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Found GSit installed!");
    }
}
