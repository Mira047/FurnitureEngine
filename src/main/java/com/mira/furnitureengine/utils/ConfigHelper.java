package com.mira.furnitureengine.utils;

import com.mira.furnitureengine.FurnitureEngine;
import org.bukkit.Material;

import java.util.Arrays;

public class ConfigHelper {
    public static final FurnitureEngine main = FurnitureEngine.getPlugin(FurnitureEngine.class);

    public static <T> T get(String key, String value, ReturnType type){
        String path = "Furniture." + key + "." + value;
        switch(type){
            case BOOLEAN:
                return (T) Boolean.valueOf(main.getConfig().getBoolean(path));
            // integer
            case INTEGER:
                return (T) Integer.valueOf(main.getConfig().getInt(path));
            // double
            case DOUBLE:
                return (T) Double.valueOf(main.getConfig().getDouble(path));
            // string
            case STRING:
                return (T) String.valueOf(main.getConfig().getString(path));
            // string list
            case STRING_LIST:
                return (T) main.getConfig().getStringList(path);
        }
        return null;
    }

}
