package com.mira.furnitureengine.legacy;


import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class ChunkLoadListener implements Listener {
    FurnitureEngine plugin = FurnitureEngine.getPlugin(FurnitureEngine.class);

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (entity instanceof ItemFrame frame) {
                // Check if the item frame is a furniture item frame (invisible and item has custom model data)
                if (frame.getItem().getType() == Material.AIR) continue;

                if (frame.isVisible() || !frame.getItem().getItemMeta().hasCustomModelData()) continue;

                // Check if it has the nbt tag called "furniture-format"
                MetadataValue furnitureFormat = frame.getMetadata("furniture-format").get(0);

                // If it has the nbt tag, skip
                if (furnitureFormat != null) continue;

                // If it doesn't have the nbt tag, add it
                frame.setMetadata("furniture-format", new FixedMetadataValue(plugin, Utils.FURNITURE_FORMAT_VERSION));

                // Move the item frame to the correct location (1 block up)
                frame.teleport(frame.getLocation().add(0, 1, 0));

                // Set to silent
                frame.setSilent(true);
            }
        }
    }
}
