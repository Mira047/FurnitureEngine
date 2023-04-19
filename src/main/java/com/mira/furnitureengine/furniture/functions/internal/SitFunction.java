package com.mira.furnitureengine.furniture.functions.internal;

import com.mira.furnitureengine.furniture.functions.Function;
import com.ranull.sittable.Sittable;
import dev.geco.gsit.api.GSitAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;

public class SitFunction implements Function {
    @Override
    public String getType() {
        return "CHAIR";
    }

    @Override
    public void execute(HashMap<String, Object> args) {
        Player player = (Player) args.get("player");
        Location location = (Location) args.get("location");

        float yOffset = args.get("y-offset") == null ? 0 : ((Double) args.get("y-offset")).floatValue();

        PluginManager pm = Bukkit.getServer().getPluginManager();

        if(pm.getPlugin("GSit") != null) {
            if (GSitAPI.getSeats(location.getBlock()).size() == 0) {
                GSitAPI.createSeat(location.getBlock(), player, true, 0, yOffset, 0, 0, true);
            }
        } else if(pm.getPlugin("Sittable") != null) {
            if (!Sittable.isBlockOccupied(location.getBlock())) {
                Sittable.sitOnBlock(player, location.getBlock(), 0.0, yOffset, 0.0, player.getFacing().getOppositeFace());
            }
        } else {
            throw new IllegalArgumentException("Missing sit plugin. Please install either GSit or Sittable.");
        }
    }
}
