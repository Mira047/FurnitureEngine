package com.mira.furnitureengine.utils;

import com.mira.furnitureengine.furnituremanager.Furniture;
import org.bukkit.Rotation;
import org.bukkit.entity.Player;

public class Utils {
    public static Rotation calculateRotation(Player player, Furniture furniture) {
        float y = player.getLocation().getYaw();
        if (y < 0) y += 360;
        y %= 360;

        // rotation identifier. Range 0-16.
        int i = (int) ((y + 8) / 22.5);

        if (furniture.getFullRotate()) {
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
}
