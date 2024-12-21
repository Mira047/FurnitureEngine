package com.mira.furnitureengine.furniture.functions.internal;

import com.mira.furnitureengine.furniture.functions.Function;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

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

        Event event = (Event) args.get("event");

        if(event instanceof Cancellable && ((Cancellable) event).isCancelled()) return;

        if(!args.containsKey("sound")) throw new IllegalArgumentException("Missing argument: sound");

        String sound = (String) args.get("sound");

        float volume = args.get("volume") == null ? 1 : ((Double) args.get("volume")).floatValue();
        float pitch = args.get("pitch") == null ? 1 : ((Double) args.get("pitch")).floatValue();

        // sound category
        String sc = (String) args.get("sc");

        if(sc != null && !sc.isEmpty()) {
            try {
                player.playSound(location, sound, SoundCategory.valueOf(sc), volume, pitch);
            } catch (Exception ignored) {}
            return;
        }

        try {
            location.getWorld().playSound(location, sound, SoundCategory.BLOCKS, volume, pitch);
        } catch (Exception ignored) {}
    }
}
