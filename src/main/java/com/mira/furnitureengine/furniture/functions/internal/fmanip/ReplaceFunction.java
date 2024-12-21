package com.mira.furnitureengine.furniture.functions.internal.fmanip;

import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.furniture.FurnitureManager;
import com.mira.furnitureengine.furniture.core.Furniture;
import com.mira.furnitureengine.furniture.functions.Function;
import com.mira.furnitureengine.furniture.functions.FunctionType;
import com.mira.furnitureengine.utils.Utils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Collection;
import java.util.HashMap;

public class ReplaceFunction implements Function {
    @Override
    public String getType() {
        return "REPLACE";
    }

    @Override
    public void execute(HashMap<String, Object> args) throws IllegalArgumentException {
        Location origin = (Location) args.get("origin");

        String furnitureOverride = (String) args.get("new");

        // Get the furniture at the origin location, get the rotation, and remove it. Then, spawn the new furniture with the same rotation.
        Furniture furniture = FurnitureManager.getInstance().isFurniture(origin);

        if(furniture == null) throw new UnknownError("No furniture found at origin location. How did this happen?");

        // rotation
        Collection<Entity> entities = origin.getWorld().getNearbyEntities(origin.clone().add(0.5, 0, 0.5), 0.2, 0.2, 0.2);

        Rotation rot = null;

        for (Entity entity : entities) {
            if (entity.getType() != EntityType.ITEM_FRAME) continue;

            ItemDisplay display = (ItemDisplay) entity;

            if (Utils.itemsMatch(display.getItemStack(), furniture.getBlockItem())) {
                // rot = ItemDisplay.getRotation();
                break;
            }
        }

        if(rot == null) {
            throw new UnknownError("No item frame found at origin location. How did this happen?");
        }

        Furniture newFurniture = FurnitureManager.getInstance().getFurniture(furnitureOverride);

        if(newFurniture == null) {
            FurnitureEngine.getInstance().getLogger().warning("Failed to replace furniture: " + furnitureOverride + " does not exist.");
            return;
        }

        Color color = Utils.getColor(origin);

        furniture.breakFurniture(null, origin);

        if(!Utils.hasSpace(origin, rot, newFurniture)) {
            // Respawn the old furniture
            furniture.spawn(origin, rot, color);

            return;
        }

        newFurniture.spawn(origin, rot, color);

        Event event = (Event) args.get("event");

        newFurniture.callFunction(
                event,
                FunctionType.REPLACE,
                (Location) args.get("location"),
                (Player) args.get("player"),
                Utils.getOriginLocation((Location) args.get("location"), newFurniture),
                Utils.getDisplay((Location) args.get("location"))
        );
    }
}
