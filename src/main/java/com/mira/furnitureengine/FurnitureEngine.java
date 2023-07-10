package com.mira.furnitureengine;

import com.mira.furnitureengine.commands.CoreCommand;
import com.mira.furnitureengine.commands.CoreCommandTabCompleter;
import com.mira.furnitureengine.furniture.FurnitureManager;
import com.mira.furnitureengine.furniture.functions.FunctionManager;
import com.mira.furnitureengine.listeners.PlayerInteractListener;
import com.mira.furnitureengine.utils.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class FurnitureEngine extends JavaPlugin {
    public static FurnitureEngine getInstance() {
        return instance;
    }

    private static FurnitureEngine instance;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("FurnitureEngine has been enabled!");

        // Load config
        loadConfig();

        // Static access... doing this so that it instantiates the classes
        FurnitureManager.getInstance();
        FunctionManager.getInstance();

        if(this.getConfig().getBoolean("Options.checkForUpdates")) {
            new UpdateChecker(97134).getVersion(version -> {
                if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                    this.getLogger().info("You are running the latest version of FurnitureEngine!");
                } else {
                    this.getLogger().info("There is a new update available for FurnitureEngine!" + " (Current version: " + this.getDescription().getVersion() + " New version: " + version + ")");
                }
            });
        }

        // Register commands
        getCommand("furnitureengine").setExecutor(new CoreCommand());
        getCommand("furnitureengine").setTabCompleter(new CoreCommandTabCompleter());

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

        // Register metrics
        new Metrics(this, 13146);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
