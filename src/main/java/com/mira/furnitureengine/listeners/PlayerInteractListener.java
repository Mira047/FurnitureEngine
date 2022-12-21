package com.mira.furnitureengine.listeners;

import com.mira.furnitureengine.events.FurnitureInteractEvent;
import com.mira.furnitureengine.furnituremanager.FurnitureDefault;
import com.mira.furnitureengine.furnituremanager.FurnitureManager;
import com.mira.furnitureengine.integrations.GSit;
import com.mira.furnitureengine.integrations.IntegrationManager;
import com.mira.furnitureengine.utils.ConfigHelper;
import com.mira.furnitureengine.utils.ReturnType;
import com.mira.furnitureengine.utils.Utils;
import dev.geco.gsit.api.GSitAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        final Player player = e.getPlayer();

        final Block clicked = e.getClickedBlock();

        // Interacting with a block
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK&& e.getHand() == EquipmentSlot.HAND && !player.isSneaking()) {
            if(!IntegrationManager.canBreak(player, clicked.getLocation())) return;

            if(!clicked.getType().equals(Material.BARRIER)) return;

            List<Entity> nearbyEntites = (List <Entity>) clicked.getWorld().getNearbyEntities(clicked.getLocation().add(0, 1, 0), 0.13, 0.2, 0.13);

            for(Entity entity : nearbyEntites) {
                if (entity instanceof ItemFrame frame) {
                    // First check if the frame item has custom model data, otherwise continue.
                    if (!frame.getItem().getItemMeta().hasCustomModelData()) continue;

                    if (frame.getLocation().getBlock().getLocation().add(0,-1,0).equals(clicked.getLocation())) {
                        for(String key : ConfigHelper.main.getConfig().getConfigurationSection("Furniture").getKeys(false)) {
                            // Check if the config and item custom model data match
                            if(ConfigHelper.main.getConfig().getInt("Furniture." + key + ".custommodeldata")==frame.getItem().getItemMeta().getCustomModelData()){
                                // Check the material of the block
                                Material type = Material.matchMaterial(ConfigHelper.get(key,"material", ReturnType.STRING));
                                // Default to Oak Planks if the material is not found
                                if(type==null) type = Material.OAK_PLANKS;
                                if(!frame.getItem().getType().equals(type)) continue;

                                FurnitureDefault furniture = FurnitureManager.getFurnitureByKey(key);

                                FurnitureInteractEvent event = new FurnitureInteractEvent(player, clicked.getLocation());
                                Bukkit.getServer().getPluginManager().callEvent((Event)event);

                                if(!event.isCancelled()) {
                                    // Managing interactions: GSit
                                    if (ConfigHelper.main.getConfig().getBoolean("Furniture." + key + ".chair.enabled")) {
                                        // System.out.println("[FurnitureEngine] Chairs are enabled " + key);
                                        if (GSit.hasGSit()) {
                                            // check if theres a player already
                                            if (GSitAPI.getSeats(clicked).size() == 0) {
                                                GSitAPI.createSeat(clicked, player, true, 0, ConfigHelper.main.getConfig().getDouble("Furniture." + key + ".chair.yoffset"), 0, 0, true);
                                            }
                                        }
                                    }

                                    // Managing interactions: executing commands
                                    if(player.isSneaking()) Utils.executeCommand("shift-right-click", player, key, clicked.getLocation());
                                    else Utils.executeCommand("right-click", player, key, clicked.getLocation());
                                }

                                return;
                            }
                        }
                    }
                }
            }
        }

        // Furniture Breaking
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if(!IntegrationManager.canBreak(player, clicked.getLocation())) return;

            if(!clicked.getType().equals(Material.BARRIER)) return;

            List<Entity> nearbyEntites = (List <Entity>) clicked.getWorld().getNearbyEntities(clicked.getLocation().add(0, 1, 0), 0.13, 0.2, 0.13);

            for(Entity entity : nearbyEntites){
                if(entity instanceof ItemFrame frame){
                    // First check if the frame item has custom model data, otherwise continue.
                    if(!frame.getItem().getItemMeta().hasCustomModelData()) continue;

                    if (frame.getLocation().getBlock().getLocation().add(0,-1,0).equals(clicked.getLocation())) {
                        for(String key : ConfigHelper.main.getConfig().getConfigurationSection("Furniture").getKeys(false)) {
                            // Check if the config and item custom model data match
                            if(ConfigHelper.main.getConfig().getInt("Furniture." + key + ".custommodeldata")==frame.getItem().getItemMeta().getCustomModelData()){
                                // Check the material of the block
                                Material type = Material.matchMaterial(ConfigHelper.get(key,"material", ReturnType.STRING));
                                // Default to Oak Planks if the material is not found
                                if(type==null) type = Material.OAK_PLANKS;
                                if(!frame.getItem().getType().equals(type)) {
                                    // System.out.println("The types did not match! The frame was " + frame.getItem().getType() + " while the furniture was " + type);
                                    continue;
                                }

                                FurnitureDefault furniture = FurnitureManager.getFurnitureByKey(key);
                                FurnitureManager.breakFurniture(furniture, clicked.getLocation(), player);

                                return;
                            }
                        }
                    }
                }

            }
        }
    }
}
