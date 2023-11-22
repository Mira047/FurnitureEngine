package com.mira.furnitureengine.furniture.functions.internal.storage;

import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.furniture.functions.Function;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class DataSetFunction implements Function {

    @Override
    public String getType() {
        return "SET";
    }

    @Override
    public void execute(HashMap<String, Object> args) throws IllegalArgumentException {
        String type = (String) args.get("variable");

        if(!args.containsKey("value")) throw new IllegalArgumentException("Missing argument: value");

        // number, string, bool, nbt-compound
        switch (type.toLowerCase()) {
            case "number" -> {
                String name = (String) args.get("name");
                double value = ((Number) args.get("value")).doubleValue();

                Entity frame = (Entity) args.get("entity");

                frame.getPersistentDataContainer().set(new NamespacedKey(FurnitureEngine.getInstance(), name), PersistentDataType.DOUBLE, value);
            }
            case "string" -> {
                String name = (String) args.get("name");
                String value = (String) args.get("value");

                Entity frame = (Entity) args.get("entity");

                frame.getPersistentDataContainer().set(new NamespacedKey(FurnitureEngine.getInstance(), name), PersistentDataType.STRING, value);
            }
            case "bool" -> {
                String name = (String) args.get("name");
                boolean value = (boolean) args.get("value");

                Entity frame = (Entity) args.get("entity");

                frame.getPersistentDataContainer().set(new NamespacedKey(FurnitureEngine.getInstance(), name), PersistentDataType.BYTE, value ? (byte) 1 : (byte) 0);
            }
//            case "nbt-compound" -> {
//                String name = (String) args.get("name");
//                String value = (String) args.get("value");
//
//                Entity frame = (Entity) args.get("entity");
//
//                frame.getPersistentDataContainer().set(new NamespacedKey(FurnitureEngine.getInstance(), name), PersistentDataType.TAG_CONTAINER, value);
//            }
        }
    }
}
