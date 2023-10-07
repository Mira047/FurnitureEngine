package com.mira.furnitureengine.listeners;

import com.mira.furnitureengine.furniture.FurnitureManager;
import com.mira.furnitureengine.furniture.core.Furniture;
import com.mira.furnitureengine.furniture.core.SubModel;
import com.mira.furnitureengine.utils.Utils;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.inventory.ItemStack;


public class HangingBreakListener implements Listener {
    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        Hanging hanging = event.getEntity();

        if(hanging instanceof ItemFrame frame) {
            ItemStack item = frame.getItem();

            for (Furniture furniture : FurnitureManager.getInstance().getFurniture()) {
                if (Utils.itemsMatch(item, furniture.getBlockItem())) {
                    event.setCancelled(true);
                    return;
                }

                // Additionally, check if any of the submodels match
                for (SubModel subModel : furniture.getSubModels()) {
                    if (Utils.itemsMatch(item, furniture.generateSubModelItem(subModel))) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}
