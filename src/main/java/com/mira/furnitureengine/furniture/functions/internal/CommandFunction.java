package com.mira.furnitureengine.furniture.functions.internal;

import com.mira.furnitureengine.furniture.core.Furniture;
import com.mira.furnitureengine.furniture.functions.Function;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandFunction implements Function {

    @Override
    public String getType() {
        return "COMMAND";
    }

    @Override
    public void execute(HashMap<String, Object> args) throws IllegalArgumentException {
        if (!args.containsKey("command")) throw new IllegalArgumentException("Missing argument: command");

        String command = (String) args.get("command");

        // placeholders
        command = command
                .replace("%player%", ((Player) args.get("player")).getName())
                .replace("%furniture%", ((Furniture) args.get("furniture")).getId())
                .replace("%location%", ((Location) args.get("location")).getX() + " " + ((Location) args.get("location")).getY() + " " + ((Location) args.get("location")).getZ())
                .replace("%world%", ((Location) args.get("location")).getWorld().getName())
                .replace("%location_x%", ((Location) args.get("location")).getX() + "")
                .replace("%location_y%", ((Location) args.get("location")).getY() + "")
                .replace("%location_z%", ((Location) args.get("location")).getZ() + "")
                .replace("%origin%", ((Location) args.get("origin")).getX() + " " + ((Location) args.get("origin")).getY() + " " + ((Location) args.get("origin")).getZ())
                .replace("%origin_x%", ((Location) args.get("origin")).getX() + "")
                .replace("%origin_y%", ((Location) args.get("origin")).getY() + "")
                .replace("%origin_z%", ((Location) args.get("origin")).getZ() + "");

        Player player = (Player) args.get("player");
        boolean isOperator = player.isOp();

        if (command.startsWith("[op]")) {
            try {
                player.setOp(true);
                player.performCommand(command.substring(4));
            } finally {
                player.setOp(isOperator);
            }
        } else if (command.startsWith("[console]")) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command.substring(9));
        } else {
            player.performCommand(command);
        }
    }
}