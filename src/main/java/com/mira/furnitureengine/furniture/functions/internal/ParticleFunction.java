package com.mira.furnitureengine.furniture.functions.internal;

import com.mira.furnitureengine.furniture.functions.Function;
import com.mira.furnitureengine.furniture.functions.FunctionType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.material.MaterialData;

import java.util.HashMap;

public class ParticleFunction implements Function {
    @Override
    public String getType() {
        return "PARTICLE";
    }

    @Override
    public void execute(HashMap<String, Object> args) throws IllegalArgumentException {
        Location location = (Location) args.get("location");

        FunctionType ftype = (FunctionType) args.get("functionType");

        Event event = (Event) args.get("event");

        if(event instanceof Cancellable && ((Cancellable) event).isCancelled()) return;
        
        switch (ftype) {
            case BREAK -> {
                location = location.clone().add(0, 0, 0);
            }
            case SUBMODEL_BREAK -> {
                location = location.clone().add(0.5, 0, 0.5);
            }
        }

        if(!args.containsKey("particle")) throw new IllegalArgumentException("Missing argument: particle");

        String type = (String) args.get("particle");

        int count = args.get("count") == null ? 1 : ((Number) args.get("count")).intValue();

        double offsetX = args.get("offsetX") == null ? 0 : ((Number) args.get("offsetX")).doubleValue();
        double offsetY = args.get("offsetY") == null ? 0 : ((Number) args.get("offsetY")).doubleValue();
        double offsetZ = args.get("offsetZ") == null ? 0 : ((Number) args.get("offsetZ")).doubleValue();

        double deltaX = args.get("deltaX") == null ? 0 : ((Number) args.get("deltaX")).doubleValue();
        double deltaY = args.get("deltaY") == null ? 0 : ((Number) args.get("deltaY")).doubleValue();
        double deltaZ = args.get("deltaZ") == null ? 0 : ((Number) args.get("deltaZ")).doubleValue();

        Location loc = location.clone().add(offsetX, offsetY, offsetZ);

        if(type.equals("BLOCK_CRACK") || type.equals("BLOCK_DUST")) {
            if(!args.containsKey("material")) throw new IllegalArgumentException("Missing argument: material");

            String data = (String) args.get("material");

            try {
                location.getWorld().spawnParticle(
                        org.bukkit.Particle.valueOf(type),
                        loc,
                        count,
                        deltaX,
                        deltaY,
                        deltaZ,
                        Material.valueOf(data).createBlockData()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                location.getWorld().spawnParticle(org.bukkit.Particle.valueOf(type), loc, count, deltaX, deltaY, deltaZ);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
