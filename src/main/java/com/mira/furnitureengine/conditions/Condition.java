package com.mira.furnitureengine.conditions;

import org.bukkit.entity.Player;

public interface Condition {
    boolean check(Player player, String[] args);
}
