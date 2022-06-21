package com.mira.furnitureengine.integrations;

import com.mira.furnitureengine.utils.ConfigHelper;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldGuard {
    private static WorldGuardPlugin worldGuardPlugin;

    public static void init(Plugin plugin) {
        worldGuardPlugin = (WorldGuardPlugin) plugin;
    }

    public static boolean hasWorldGuard() {
        if(worldGuardPlugin == null) return false;

        return true;
    }

    public static boolean canBuild(Player player, Location placedLocation) {
        LocalPlayer localPlayer = worldGuardPlugin.inst().wrapPlayer(player);

        RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        return query.testBuild(BukkitAdapter.adapt(placedLocation), localPlayer, Flags.BLOCK_PLACE);
    }

    public static boolean canBreak(Player player, Location placedLocation) {
        LocalPlayer localPlayer = worldGuardPlugin.inst().wrapPlayer(player);

        RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        return query.testBuild(BukkitAdapter.adapt(placedLocation), localPlayer, Flags.BLOCK_BREAK);
    }
}
