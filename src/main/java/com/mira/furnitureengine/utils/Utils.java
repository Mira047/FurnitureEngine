package com.mira.furnitureengine.utils;


import com.mira.furnitureengine.furniture.core.Furniture;
import com.mira.furnitureengine.furniture.core.SubModel;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;

public class Utils {
    private final static int FURNITURE_FORMAT_VERSION = 4;

    public final static NamespacedKey FURNITURE_ID_KEY = new NamespacedKey("furnitureengine", "furniture_id");


    public static int getFurnitureFormatVersion() {
        return FURNITURE_FORMAT_VERSION;
    }

    public static boolean itemsMatch(ItemStack item1, ItemStack item2) {
        if(item1 == null || item2 == null) return false;

        if(item1.getType() != item2.getType()) return false;

        if(item1.hasItemMeta() && item2.hasItemMeta()) {
            if(item1.getItemMeta().hasCustomModelData() && item2.getItemMeta().hasCustomModelData()) {
                return item1.getItemMeta().getCustomModelData() == item2.getItemMeta().getCustomModelData();
            }
        }

        return false;
    }

    public static Location calculatePlacingLocation(Block clickedBlock, BlockFace clickedFace) {
        Location location = clickedBlock.getLocation();

        if(!Utils.isSolid(clickedBlock)) return location;

        switch (clickedFace) {
            case UP -> {
                location.add(0, 1, 0);
            }
            case DOWN -> {
                location.add(0, -1, 0);
            }
            case NORTH -> {
                location.add(0, 0, -1);
            }
            case SOUTH -> {
                location.add(0, 0, 1);
            }
            case WEST -> {
                location.add(-1, 0, 0);
            }
            case EAST -> {
                location.add(1, 0, 0);
            }
        }

        return location;
    }

    /**
     * Used to check if furniture (presumably with submodels) can space to be placed at a specific location
     * @param location The location to check
     * @param rotation The rotation of the furniture
     * @param furniture The furniture to check
     * @return Whether the furniture can be placed or not
     */
    public static boolean hasSpace(Location location, Rotation rotation, Furniture furniture) {
        if(Utils.isSolid(location.getBlock())) return false;

        if(furniture.getSubModels().isEmpty()) return true;

        for(SubModel subModel : furniture.getSubModels()) {
            Location subModelLocation = Utils.getRelativeLocation(location, subModel.getOffset(), rotation);
            if(Utils.isSolid(subModelLocation.getBlock())) return false;
        }

        return true;
    }

    /**
     * Used to get the location of a submodel from the origin
     * @param input The origin
     * @param offset The offset of the submodel
     * @param rotation The rotation of the item
     * @return The relative location
     */
    public static Location getRelativeLocation(Location input, Vector offset, Rotation rotation) {
        switch(rotation) {
            case CLOCKWISE -> {
                return input.clone().add(-offset.getZ(), offset.getY(), offset.getX());
            }
            case FLIPPED -> {
                return input.clone().add(-offset.getX(), offset.getY(), -offset.getZ());
            }
            case COUNTER_CLOCKWISE -> {
                return input.clone().add(offset.getZ(), offset.getY(), -offset.getX());
            }
            default -> {
                return input.clone().add(offset);
            }
        }
    }

    /**
     * Used to get the location of the origin from a submodel
     * @param input The current location
     * @param furniture The furniture
     * @return The relative location
     */
    public static Location getOriginLocation(Location input, Furniture furniture) {
        // Check for item frames
        Collection<Entity> entities = input.getWorld().getNearbyEntities(input.clone().add(0.5, 0.5, 0.5), 0.1, 0.1, 0.1);
        for(Entity entity : entities) {
            if(entity instanceof ItemDisplay display) {
                // Get the item and compare it to the furniture
                if(itemsMatch(display.getItemStack(), furniture.getBlockItem())) {
                    return input.clone();
                }
                else {
                    for(SubModel subModel : furniture.getSubModels()) {
                        if(itemsMatch(display.getItemStack(), furniture.generateSubModelItem(subModel))) {
                            float y = display.getLocation().getYaw();
                            Rotation rotation = Utils.rotationFromAngle(y);

                            Vector offset = subModel.getOffset().clone();

                            switch (rotation) {
                                case CLOCKWISE -> {
                                    return input.clone().subtract(-offset.getZ(), offset.getY(), offset.getX());
                                }
                                case FLIPPED -> {
                                    return input.clone().subtract(-offset.getX(), offset.getY(), -offset.getZ());
                                }
                                case COUNTER_CLOCKWISE -> {
                                    return input.clone().subtract(offset.getZ(), offset.getY(), -offset.getX());
                                }
                                default -> {
                                    return input.clone().subtract(offset);
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public static Entity getDisplay(Location loc) {
        Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 0.1, 0.1, 0.1);
        for(Entity entity : entities) {
            if(entity instanceof ItemDisplay display) {
                return display;
            }
        }
        return null;
    }

    public static float angleFromRotation(Rotation rot) {
        switch (rot) {
            case NONE -> {
                return 0;
            }
            case CLOCKWISE_45 -> {
                return 45;
            }
            case CLOCKWISE -> {
                return 90;
            }
            case CLOCKWISE_135 -> {
                return 135;
            }
            case FLIPPED -> {
                return 180;
            }
            case COUNTER_CLOCKWISE -> {
                return 270;
            }
            case COUNTER_CLOCKWISE_45 -> {
                return 315;
            }
            case FLIPPED_45 -> {
                return 225;
            }
        }

        return 0;
    }

    public static Rotation rotationFromAngle(float angle) {
        // just in case it actualy isnt a proper angle :scream:
        angle = ((angle % 360) + 360) % 360;

        // math is mathing
        int index = Math.round(angle / 45) % 8;

        // oh fuck kinda cool
        return switch (index) {
            case 0 -> Rotation.NONE;              // 0
            case 1 -> Rotation.CLOCKWISE_45;      // 45
            case 2 -> Rotation.CLOCKWISE;         // 90
            case 3 -> Rotation.CLOCKWISE_135;     // 135
            case 4 -> Rotation.FLIPPED;           // 180
            case 5 -> Rotation.FLIPPED_45;        // 225
            case 6 -> Rotation.COUNTER_CLOCKWISE; // 270
            case 7 -> Rotation.COUNTER_CLOCKWISE_45; // 315
            default -> Rotation.NONE;             // :whale:
        };
    }

    public static Rotation getRotation(Entity entity, Furniture.RotSides rotSides) {
        float y = entity.getLocation().getYaw();
        if (y < 0) y += 360;
        y %= 360;

        int i = (int) ((y + 8) / 22.5);

        if(rotSides == Furniture.RotSides.FOUR_SIDED) {
            switch (i) {
                case 14, 15, 16, 0, 1 -> {
                    return Rotation.FLIPPED;
                }
                case 2, 3, 4,5 -> {
                    return Rotation.COUNTER_CLOCKWISE;
                }
                case 6, 7, 8, 9 -> {
                    return Rotation.NONE;
                }
                case 10, 11, 12, 13 -> {
                    return Rotation.CLOCKWISE;
                }
            }
        } else if(rotSides == Furniture.RotSides.EIGHT_SIDED) {
            switch(i) {
                case 15,16,0,1 -> {
                    return Rotation.FLIPPED;
                }
                case 2 -> {
                    return Rotation.FLIPPED_45;
                }
                case 3,4,5 -> {
                    return Rotation.COUNTER_CLOCKWISE;
                }
                case 6 -> {
                    return Rotation.COUNTER_CLOCKWISE_45;
                }
                case 7,8,9 -> {
                    return Rotation.NONE;
                }
                case 10 -> {
                    return Rotation.CLOCKWISE_45;
                }
                case 11,12,13 -> {
                    return Rotation.CLOCKWISE;
                }
                case 14 -> {
                    return Rotation.CLOCKWISE_135;
                }
            }
        } else {
            return Rotation.NONE;
        }

        return Rotation.NONE;
    }

    public static boolean entityObstructing(Location location) {
        // Check if there is an entity obstructing the location (but item frames get ignored)
        for(Entity entity : location.getWorld().getNearbyEntities(location.add(0.5, 0.5, 0.5), 0.5, 0.5, 0.5)) {
            if(entity.getType().isAlive() && entity.getType() != EntityType.ITEM_DISPLAY) {
                return true;
            }
        }

        return false;
    }

    /*
        Apparently the Material.isSolid() method is not exactly what I need... For example flowers are considered non-solid,
        but you shouldn't be able to place furniture on them. So I made my own method.
     */
    public static boolean isSolid(Block block) {
        if(block.getType() == Material.WATER) return false;

        if(block.getType().isSolid()) return true;

        else {
            if(block.getType().getBlastResistance() > 0.2) return true;

            if(block.getType().name().contains("SAPLING")) return true;

            if(block.getType().name().contains("FLOWER") || block.getType().name().contains("TULIP")) return true;

            if(block.getType().name().contains("CARPET")) return true;

            if(block.getType().name().contains("MUSHROOM") || block.getType().name().contains("FUNGUS")) return true;

            if(block.getType().name().contains("BANNER")) return true;

            switch (block.getType()) {
                case TORCH, SOUL_TORCH, LANTERN, SOUL_LANTERN, REDSTONE_WIRE, REDSTONE, REDSTONE_TORCH, REDSTONE_WALL_TORCH, NETHER_PORTAL, END_PORTAL, BEETROOTS, CARROTS, POTATOES, WHEAT, SWEET_BERRY_BUSH, SCAFFOLDING, PUMPKIN_STEM, MELON_STEM, NETHER_WART, FLOWER_POT, END_ROD, KELP, DANDELION, POPPY, BLUE_ORCHID, ALLIUM, AZURE_BLUET, OXEYE_DAISY, CORNFLOWER, LILY_OF_THE_VALLEY, WITHER_ROSE, COBWEB -> {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * It's the same case as the isSolid() method, but for interactable blocks.
     * Because for SOME REASON STAIRS ARE INTERACTABLE??? WHAT THE HELL MOJANG
     * @param block The block to check
     * @return Whether the block is interactable or not
     */
    public static boolean isInteractable(Block block) {
        if(!block.getType().isInteractable()) return false;

        else {
            if(block.getType().name().contains("STAIRS")) return false;

            if(block.getType().name().contains("TNT")) return false;

            if(block.getType().name().contains("FENCE")) return false;

            if(block.getType().name().contains("IRON")) return false; // iron door, iron trapdoor

            return true;
        }
    }

    /**
     * Gets the color of furniture, if it is colorable
     * @param location The location of the furniture
     * @return The color of the block, or null if it is not colorable
     */
    public static Color getColor(Location location) {
        Collection<Entity> entities = location.getWorld().getNearbyEntities(location, 0.2, 0.2, 0.2);

        for (Entity entity : entities) {
            if (entity.getType() != EntityType.ITEM_FRAME) continue;

            ItemDisplay display = (ItemDisplay) entity;

            if (display.getItemStack().getType() == Material.TIPPED_ARROW) {
                PotionMeta potionMeta = (PotionMeta) display.getItemStack().getItemMeta();

                if (potionMeta.hasColor()) {
                    return potionMeta.getColor();
                } else return null;
            }
        }

        return null;
    }

    /**
     * Checks if the furniture is only vertical (no sideways submodels)
     * @param subModels The submodels to check
     * @return Whether the furniture is only vertical or not
     */
    public static boolean onlyVertical(List<SubModel> subModels) {
        for(SubModel subModel : subModels) {
            if(subModel.getOffset().getX() != 0 || subModel.getOffset().getZ() != 0) {
                return false;
            }
        }

        return true;
    }
}
