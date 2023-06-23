package com.mira.furnitureengine.furniture.functions.internal;

import com.mira.furnitureengine.furniture.functions.Function;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SwingFunction implements Function {
    @Override
    public String getType() {
        return "SWING_ARM";
    }

    @Override
    public void execute(HashMap<String, Object> args) throws IllegalArgumentException {
        Player player = (Player) args.get("player");

        if(!args.containsKey("arm")) throw new IllegalArgumentException("Missing argument: arm");

        String arm = (String) args.get("arm");

        if(arm.equalsIgnoreCase("right")) {
            player.swingMainHand();
        } else if(arm.equalsIgnoreCase("left")) {
            player.swingOffHand();
        } else {
            throw new IllegalArgumentException("Invalid argument: arm");
        }

    }
}
