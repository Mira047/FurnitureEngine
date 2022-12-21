package com.mira.furnitureengine.listeners;

import com.mira.furnitureengine.furnituremanager.FurnitureDefault;
import com.mira.furnitureengine.furnituremanager.FurnitureManager;
import com.mira.furnitureengine.integrations.IntegrationManager;
import com.mira.furnitureengine.utils.ConfigHelper;
import com.mira.furnitureengine.utils.ReturnType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockPlaceListener implements Listener {
    // TODO: Rework placing furniture using NMS

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent e) {
        // fallback to BlockPlaceEvent if ProtocolLib is not installed
        Player player = e.getPlayer();

        Block blockPlaced = e.getBlockPlaced();

        // Get the hand the item was placed by.
        EquipmentSlot hand = e.getHand();

        // get the item in the equipment slot
        ItemStack item = hand == EquipmentSlot.HAND ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();

        ItemMeta meta = item.getItemMeta();

        Location location = blockPlaced.getLocation();

        if(!meta.hasCustomModelData()) return;

        // System.out.println("The item has custom model data. Everything is fine so far.");

        for(String key : ConfigHelper.main.getConfig().getConfigurationSection("Furniture").getKeys(false)) {
            // Check for the block type
            Material type = Material.matchMaterial(ConfigHelper.get(key,"material", ReturnType.STRING));
            // Default to Oak Planks if the material is not found
            if(type==null) type = Material.OAK_PLANKS;
            if(!blockPlaced.getType().equals(type)) return;

            // Plugin will check for the item custom model data. If it is equal to 0, it will check for the block custom model data.
            int customModelData = 0;

            if(ConfigHelper.main.getConfig().getInt("Furniture." + key + ".item-custommodeldata") == 0) {
                customModelData = ConfigHelper.main.getConfig().getInt("Furniture." + key + ".custommodeldata");
            } else customModelData = ConfigHelper.main.getConfig().getInt("Furniture." + key + ".item-custommodeldata");

            // if customModelData is equal to 0, something went wrong: throw an error.
            if(customModelData == 0) {
                ConfigHelper.main.getLogger().info(ChatColor.RED + "Error: Custom Model Data is 0. Please check your config.");

                e.setCancelled(true);
                // System.out.println("Unfortunately we have to return here.");
                return;
            }

            // System.out.println("Great news! Custom Model Data is not 0. Everything is fine so far.");

            // if the item custom model data is the same as the one in the config, place the block.
            if(meta.getCustomModelData() == customModelData) {
                // System.out.println("Generating furniture...");
                FurnitureDefault furniture = FurnitureManager.getFurnitureByKey(key);

                // Throw an exception if the furniture is null.
                if(furniture == null) {
                    throw new NullPointerException("Furniture is null.");
                }

                // Check if the block above is solid.
                if(blockPlaced.getRelative(0, 1, 0).getType().isSolid()) {
                    e.setCancelled(true);
                    return;
                }

                // System.out.println("We reached the Furnituremanager.placeFurniture() method.");

                if(!IntegrationManager.canBuild(player, location)) {
                    e.setCancelled(true);
                    return;
                }

                FurnitureManager.placeFurniture(furniture, location, null, player);

                return;
            }
        }

        // System.out.println("It appears we are out of the loop. What a shame.");
    }
}
