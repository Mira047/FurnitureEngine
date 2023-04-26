package com.mira.furnitureengine.furniture.functions.internal;

import com.mira.furnitureengine.furniture.functions.Function;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SoundFunction implements Function {
    @Override
    public String getType() {
        return "SOUND";
    }

    @Override
    public void execute(HashMap<String, Object> args) {
        Player player = (Player) args.get("player");
        Location location = (Location) args.get("location");

        if(!args.containsKey("sound")) throw new IllegalArgumentException("Missing argument: sound");

        String sound = (String) args.get("sound");


        float volume = args.get("volume") == null ? 1 : ((Double) args.get("volume")).floatValue();
        float pitch = args.get("pitch") == null ? 1 : ((Double) args.get("pitch")).floatValue();

        try {
            location.getWorld().playSound(location, sound, volume, pitch);
        } catch (Exception ignored) {}
    }
}
