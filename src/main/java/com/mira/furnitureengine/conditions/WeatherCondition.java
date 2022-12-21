package com.mira.furnitureengine.conditions;

import org.bukkit.entity.Player;

public class WeatherCondition implements Condition {
    @Override
    public boolean check(Player player, String[] args) {
        if (args.length == 0) {
            return false;
        }
        String weather = args[0];
        if (weather.equalsIgnoreCase("sunny")) {
            return player.getWorld().hasStorm();
        } else if (weather.equalsIgnoreCase("raining")) {
            return !player.getWorld().hasStorm();
        }
        return false;
    }

}
