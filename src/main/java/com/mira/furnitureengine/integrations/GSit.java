package com.mira.furnitureengine.integrations;

import dev.geco.gsit.GSitMain;
import org.bukkit.plugin.Plugin;

public class GSit {
    private static GSitMain gSitPlugin;

    public static void init(Plugin plugin) {
        gSitPlugin = (GSitMain) plugin;
    }

    public static boolean hasGSit() {
        return gSitPlugin != null;
    }
}
