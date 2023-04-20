package com.mira.furnitureengine.listeners;

import com.mira.furnitureengine.furniture.FurnitureManager;
import com.mira.furnitureengine.furniture.core.Furniture;
import com.mira.furnitureengine.furniture.functions.FunctionType;
import com.mira.furnitureengine.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
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
                // Check if the block is a furniture block
                Furniture furniture = FurnitureManager.getInstance().isFurniture(event.getClickedBlock().getLocation());

                if(furniture != null) {
                    if(furniture.callFunction(
                            FunctionType.RIGHT_CLICK,
                            event.getClickedBlock().getLocation(),
                            player
                    )) return;
                }

                if(Utils.isInteractable(event.getClickedBlock())) return;
            } else {
                Furniture furniture = FurnitureManager.getInstance().isFurniture(event.getClickedBlock().getLocation());

                if(furniture != null) {
                    furniture.callFunction(
                            FunctionType.SHIFT_RIGHT_CLICK,
                            event.getClickedBlock().getLocation(),
                            player
                    );
                }
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
                        furniture.place(player, hand, Utils.calculatePlacingLocation(event.getClickedBlock(), event.getBlockFace()));
                        return;
                    }
                }
            }
        }

        // 2. Breaking
        if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Furniture furniture = FurnitureManager.getInstance().isFurniture(event.getClickedBlock().getLocation());

            if(furniture != null) {
                if(player.isSneaking()) {
                    furniture.callFunction(
                            FunctionType.SHIFT_LEFT_CLICK,
                            event.getClickedBlock().getLocation(),
                            player
                    );
                } else {
                    furniture.callFunction(
                            FunctionType.LEFT_CLICK,
                            event.getClickedBlock().getLocation(),
                            player
                    );
                }

                Location origin = Utils.getOriginLocation(event.getClickedBlock().getLocation(), furniture);

                if(origin != null) {
                    furniture.breakFurniture(player, origin);
                }
            }
        }
    }
}
