package com.mira.furnitureengine.furniture.functions;


import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.furniture.core.Furniture;
import com.mira.furnitureengine.furniture.functions.internal.*;
import com.mira.furnitureengine.furniture.functions.internal.fmanip.ReplaceFunction;
import com.mira.furnitureengine.furniture.functions.internal.storage.DataSetFunction;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.HashMap;

public class FunctionManager {
    private static FunctionManager instance;

    HashMap<String, Function> functions = new HashMap<>();

    private FunctionManager() {
        register(new CommandFunction());
        register(new SoundFunction());
        register(new SitFunction());
        register(new SwingFunction());
        register(new ReplaceFunction());
        register(new ParticleFunction());
        register(new DataSetFunction());
    }

    public static FunctionManager getInstance() {
        if(instance == null) {
            instance = new FunctionManager();
        }
        return instance;
    }

    public void register(Function function) {
        functions.put(function.getType(), function);

        FurnitureEngine.getInstance().getLogger().info("Registered function: " + function.getType());
    }

    public void call(Event event, String type, HashMap<String, Object> args, Player player, Furniture furniture, Location interactLocation, Location originLocation, Entity frame) {
        HashMap<String, Object> argsCopy = new HashMap<>(args);

        argsCopy.put("event", event);

        argsCopy.put("player", player);

        argsCopy.put("furniture", furniture);

        argsCopy.put("location", interactLocation);

        argsCopy.put("origin", originLocation);

        argsCopy.put("entity", frame);

        try {
            functions.get(type).execute(argsCopy);
        } catch (IllegalArgumentException e) {
            FurnitureEngine.getInstance().getLogger().warning("Failed to execute function: " + e.getMessage());
        }
    }
}
