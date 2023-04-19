package com.mira.furnitureengine.furniture.core;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

// Submodel contains vector (offset) and custom model data (int)
public class SubModel implements ConfigurationSerializable {
    protected final Vector offset;
    protected final int model_data;

    public SubModel(Vector offset, int customModelData) {
        this.offset = offset;
        this.model_data = customModelData;
    }

    public Vector getOffset() {
        return offset;
    }

    public int getCustomModelData() {
        return model_data;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("offset", offset);
        map.put("model_data", model_data);

        return map;
    }

    public static SubModel deserialize(Map<String, Object> map) {
        return new SubModel((Vector) map.get("offset"), (int) map.get("model_data"));
    }

    @Override
    public String toString() {
        return "SubModel{" +
                "offset=" + offset +
                ", model_data=" + model_data +
                '}';
    }
}