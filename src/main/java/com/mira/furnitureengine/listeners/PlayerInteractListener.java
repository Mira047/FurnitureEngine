package com.mira.furnitureengine.listeners;

import com.mira.furnitureengine.Events.FurnitureInteractEvent;
import com.mira.furnitureengine.furnituremanager.Furniture;
import com.mira.furnitureengine.furnituremanager.FurnitureManager;
import com.mira.furnitureengine.integrations.IntegrationManager;
import com.mira.furnitureengine.utils.ConfigHelper;
import com.mira.furnitureengine.utils.ReturnType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        final Player player = e.getPlayer();

        final Block clicked = e.getClickedBlock();

        // Interacting with a block
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK&& e.getHand() == EquipmentSlot.HAND && !player.isSneaking()) {
            // For backwards compatibility with graves
            assert clicked != null;
            Location blockLocation = clicked.getLocation();
            World world = clicked.getWorld();

            if(clicked.getType()==Material.BARRIER) {
                List<Entity> nearbyEntites = (List<Entity>) world.getNearbyEntities(blockLocation.add(0, 1, 0), 0.5, 0.5, 0.5);
                for (Entity nearbyEntity : nearbyEntites) {
                    if (nearbyEntity instanceof ItemFrame) {
                        ItemFrame frame = (ItemFrame) nearbyEntity;
                        if(frame.getItem().getType()==Material.OAK_PLANKS) {
                            ItemMeta meta = frame.getItem().getItemMeta();
                            ConfigHelper.main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
                                if(frame.getLocation().getBlock().getLocation().getY()-1==clicked.getLocation().getY()&&frame.getLocation().getBlock().getLocation().getX()==clicked.getLocation().getX()&&frame.getLocation().getBlock().getLocation().getZ()==clicked.getLocation().getZ()) {
                                    if(meta.getCustomModelData()==ConfigHelper.main.getConfig().getInt("Furniture." + key+".custommodeldata")) {
                                        FurnitureInteractEvent event = new FurnitureInteractEvent(player, frame.getLocation().getBlock().getLocation());
                                        Bukkit.getServer().getPluginManager().callEvent(event);
                                    }
                                }
                                return;
                            });
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

                                Furniture furniture = FurnitureManager.getFurnitureByKey(key);
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
