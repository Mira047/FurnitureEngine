package com.mira.furnitureengine.listeners;

import com.mira.furnitureengine.furniture.FurnitureManager;
import com.mira.furnitureengine.furniture.core.Furniture;
import com.mira.furnitureengine.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Ignore on offhand
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        Player player = event.getPlayer();

        // 1. Placing
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Ignore if it interacts with a block (eg. opening a chest, crafting table, door, etc.)
            if(!player.isSneaking()) {
                if(Utils.isInteractable(event.getClickedBlock())) return;
            }
            // Check if the item in the player's hand is a furniture item
            EquipmentSlot hand = EquipmentSlot.HAND;
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) {
                item = player.getInventory().getItemInOffHand();
                hand = EquipmentSlot.OFF_HAND;
            }
            if (item.getType() == Material.AIR) return;

            // Check if the item is a furniture item
            if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
                for (Furniture  furniture : FurnitureManager.getInstance().getFurniture()) {
                    if (Utils.itemsMatch(item, furniture.getGeneratedItem())) {
                        furniture.place(player, hand,  Utils.calculatePlacingLocation(event.getClickedBlock(), event.getBlockFace()));
                        return;
                    }
                }
            }
        }
    }
}
