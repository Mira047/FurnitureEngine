package com.mira.furnitureengine.integrations;

import com.mira.furnitureengine.utils.ConfigHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class IntegrationManager {
    public static boolean canBuild(Player player, Location location) {
        // Admin override
        if (player.hasPermission("furnitureengine.admin")) return true;

        // Permissions
        if(!player.hasPermission("furnitureengine.placeblock")&& ConfigHelper.main.getConfig().getBoolean("check-place-permissions")) return false;

        // WorldGuard
        if(WorldGuard.hasWorldGuard() && !WorldGuard.canBuild(player, location)) return false;

        return true;
    }

    public static boolean canBreak(Player player, Location location) {
        // Admin override
        if (player.hasPermission("furnitureengine.admin")) return true;

        // Permissions
        if(!player.hasPermission("furnitureengine.breakblock")&& ConfigHelper.main.getConfig().getBoolean("check-break-permissions")) return false;

        // WorldGuard
        if(WorldGuard.hasWorldGuard() && !WorldGuard.canBreak(player, location)) return false;

        return true;
    }

    public static boolean canInteract(Player player, Location location) {
        // Admin override
        if (player.hasPermission("furnitureengine.admin")) return true;

        return true;
    }
}
