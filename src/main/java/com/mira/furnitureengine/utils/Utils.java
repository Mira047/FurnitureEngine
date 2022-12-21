package com.mira.furnitureengine.utils;

import com.mira.furnitureengine.furnituremanager.FurnitureDefault;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Utils {
    public static Rotation calculateRotation(Player player, FurnitureDefault furniture) {
        float y = player.getLocation().getYaw();
        if (y < 0) y += 360;
        y %= 360;

        // rotation identifier. Range 0-16.
        int i = (int) ((y + 8) / 22.5);

        if (furniture.isFullRotateEnabled()) {
            // 8 Side Rotation
            // West
            if (i == 15 || i == 0 || i == 1 || i == 16) return (Rotation.FLIPPED);
            // North-West
            else if (i == 2) return (Rotation.FLIPPED_45);
            // North
            else if (i == 3 || i == 4 || i == 5) return (Rotation.COUNTER_CLOCKWISE);
            // North-East
            else if (i == 6) return (Rotation.COUNTER_CLOCKWISE_45);
            // South-East
            else if (i == 10) return (Rotation.CLOCKWISE_45);
            // South
            else if (i == 11 || i == 12 || i == 13) return (Rotation.CLOCKWISE);
            // South-West
            else if (i == 14) return (Rotation.CLOCKWISE_135);
            // East
            else if (i == 7 || i == 8 || i == 9) return (Rotation.NONE);


        } else {
            // 4 Side Rotation
            if (i == 6 || i == 7 || i == 8 || i == 9) return (Rotation.NONE);
            else if (i == 10 || i == 11 || i == 12 || i == 13 || i == 14) return (Rotation.CLOCKWISE);
            else if (i == 2 || i == 3 || i == 4 || i == 5) return (Rotation.COUNTER_CLOCKWISE);
            else if (i == 15 || i == 16 || i == 0 || i == 1) return (Rotation.FLIPPED);
        }
        return null;
    }

    public static void executeCommand(String mode, Player player, String key, @Nullable Location loc) {
        List<String> commands = ConfigHelper.main.getConfig().getStringList("Furniture." + key + ".commands." + mode);
        if (commands.isEmpty())
            return;
        for (String executable : commands) {
            executable = executable.replace("<player>", player.getName());
            assert loc != null;
            executable = executable.replace("<location>", loc.getX() + " " + loc.getY() + " " + loc.getZ());
            boolean isOp = player.isOp();
            if (executable.startsWith("[op]")) {
                player.setOp(true);
                try {
                    player.performCommand(executable.substring(4));
                } finally {
                    if (!isOp) player.setOp(false);
                }
                continue;
            }
            if (executable.startsWith("[c]")) {
                Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), executable.substring(3));
                continue;
            }
            player.performCommand(executable);
        }
    }
}
