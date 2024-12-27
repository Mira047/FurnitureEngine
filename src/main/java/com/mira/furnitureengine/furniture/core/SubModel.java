package com.mira.furnitureengine.furniture.core;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

// Submodel contains vector (offset) and custom model data (int)
public class SubModel implements ConfigurationSerializable {
    protected final Vector offset;
    protected final int model_data;
    protected boolean visible = true;

    public SubModel(Vector offset, int customModelData) {
        this.offset = offset;
        this.model_data = customModelData;
    }

    public SubModel(Vector offset, int customModelData, boolean visible) {
        this.offset = offset;
        this.model_data = customModelData;
        this.visible = visible;
    }

    public Vector getOffset() {
        return offset;
    }

    public int getCustomModelData() {
        return model_data;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("offset", offset);
        map.put("model_data", model_data);
        map.put("visible", visible);

        return map;
    }

    public static SubModel deserialize(Map<String, Object> map) {
        return new SubModel((Vector) map.get("offset"), (int) map.get("model_data"), (boolean) map.getOrDefault("visible", true));
    }

    @Override
    public String toString() {
        return "SubModel{" +
                "offset=" + offset +
                ", model_data=" + model_data +
                ", visible=" + visible +
                '}';
    }
}