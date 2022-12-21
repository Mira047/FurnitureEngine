package com.mira.furnitureengine.furnituremanager;

import com.mira.furnitureengine.events.FurnitureBreakEvent;
import com.mira.furnitureengine.events.FurniturePlaceEvent;
import com.mira.furnitureengine.utils.ConfigHelper;
import com.mira.furnitureengine.utils.ItemUtils;
import com.mira.furnitureengine.utils.ReturnType;
import com.mira.furnitureengine.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FurnitureManager {
    // instantiates a new Furniture object by the key.
    public static FurnitureDefault getFurnitureByKey(@NotNull String key) {
        return new FurnitureDefault(
                Material.matchMaterial(ConfigHelper.get(key, "material", ReturnType.STRING)) == null ? Material.OAK_PLANKS : Material.matchMaterial(ConfigHelper.get(key, "material", ReturnType.STRING)),
                key,
                ConfigHelper.get(key, "display", ReturnType.STRING),
                ConfigHelper.get(key,"width", ReturnType.INTEGER),
                ConfigHelper.get(key,"length", ReturnType.INTEGER),
                ConfigHelper.get(key,"height", ReturnType.INTEGER),
                ConfigHelper.get(key,"custommodeldata", ReturnType.INTEGER),
                ConfigHelper.get(key,"item-custommodeldata", ReturnType.INTEGER),
                ConfigHelper.get(key, "chair.enabled", ReturnType.BOOLEAN),
                ConfigHelper.get(key, "chair.yoffset", ReturnType.DOUBLE),
                ConfigHelper.get(key, "full-rotate", ReturnType.BOOLEAN),
                ConfigHelper.get(key, "addons.light-level", ReturnType.INTEGER),
                null
        );
    }

    public static FurnitureDefault getFurnitureByItem(@NotNull ItemStack item) {
        for(String key : ConfigHelper.main.getConfig().getConfigurationSection("Furniture").getKeys(false)) {
            Material type = Material.matchMaterial(ConfigHelper.get(key, "material", ReturnType.STRING)) == null ? Material.OAK_PLANKS : Material.matchMaterial(ConfigHelper.get(key, "material", ReturnType.STRING));
            int customModelData = ConfigHelper.get(key, "custommodeldata", ReturnType.INTEGER);

            if(item.getType() == type && item.getItemMeta().getCustomModelData() == customModelData) {
                return getFurnitureByKey(key);
            }
        }
        return null;
    }

    // Places furniture at a location
    public static void placeFurniture(@NotNull FurnitureDefault furniture, @NotNull Location location, @Nullable Rotation rotation, @Nullable Player player) {
        Block block = location.getBlock();

        FurniturePlaceEvent event = new FurniturePlaceEvent(furniture, player, location);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if(!event.isCancelled()) {
            // Calculates the rotation based on the player's rotation if it is null
            if(rotation == null) {
                if(player != null) {
                    rotation = Utils.calculateRotation(player, furniture);
                }
            }

            // Throws an exception if rotation is still null
            if(rotation == null) {
                throw new IllegalArgumentException("Rotation cannot be null");
            }

            furniture.place(block, rotation);
        }
    }

    // Breaks Furniture at a location
    public static void breakFurniture(@NotNull FurnitureDefault furniture, @NotNull Location location, @Nullable Player player) {
        Block block = location.getBlock();

        FurnitureBreakEvent event = new FurnitureBreakEvent(furniture, player, location);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if(!event.isCancelled()) {
            furniture.destroy(block);

            location.getWorld().playSound(location, Sound.BLOCK_WOOD_BREAK, 3, 1);

            if(ConfigHelper.get(furniture.id,"cancel-item-drop",ReturnType.BOOLEAN)) event.setDroppingItems(false);
            if(event.isDroppingItems()) {
                // Extra creative mode check
                if(player != null && player.getGameMode() != GameMode.CREATIVE)
                    location.getWorld().dropItemNaturally(location, ItemUtils.setItem(furniture.id,1));
            }
        }
    }

    // Only for compatibility with other plugins.
    @Deprecated
    public static void breakFurniture(@NotNull Location location){
        Block block = location.getBlock();

        FurnitureBreakEvent event = new FurnitureBreakEvent(null, null, location);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if(!event.isCancelled()) {
            if(block.getType()==Material.BARRIER) {
                List<Entity> nearbyEntites = (List<Entity>) block.getWorld().getNearbyEntities(block.getLocation().add(0, 1, 0), 0.13, 0.2, 0.13);
                for (Entity nearbyEntity : nearbyEntites) {
                    if (nearbyEntity instanceof ItemFrame) {
                        ItemFrame frame = (ItemFrame) nearbyEntity;
                        if(frame.getItem().getType()==Material.OAK_PLANKS) {
                            for(String key : ConfigHelper.main.getConfig().getConfigurationSection("Furniture").getKeys(false)) {
                                if(ConfigHelper.main.getConfig().getInt("Furniture." + key + ".custommodeldata")==frame.getItem().getItemMeta().getCustomModelData()) {
                                    if(frame.getLocation().getBlock().getLocation().getY()-1==block.getLocation().getY()&&frame.getLocation().getBlock().getLocation().getX()==block.getLocation().getX()&&frame.getLocation().getBlock().getLocation().getZ()==block.getLocation().getZ()) {
                                        frame.remove();
                                        block.breakNaturally();
                                        Location loc = block.getLocation();

                                        block.getWorld().playSound(loc, Sound.BLOCK_WOOD_BREAK, 3, 1);
                                        if(event.isDroppingItems()) {
                                            ItemUtils.giveItem(null, key, 1, loc);
                                        }

                                    }
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}