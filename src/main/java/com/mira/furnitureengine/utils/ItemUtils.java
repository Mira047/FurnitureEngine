package com.mira.furnitureengine.utils;

import com.mira.furnitureengine.FurnitureEngine;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemUtils {
    static FurnitureEngine main = FurnitureEngine.getPlugin(FurnitureEngine.class);

    /* Item Giving */
    public static void giveItem(Player player, String id, int amount, Location loc) {
        if(loc==null) {
            // if no location is provided it will default to player location
            loc = player.getLocation();
        }

        ItemStack item = setItem(id, amount);

        if(player==null) {
            dropItem(item, loc);
            return;
        }
        if(!player.getInventory().addItem(item).isEmpty()) {
            dropItem(item, loc);
        }

        return;
    }

    public static void dropItem(ItemStack item, Location loc) {
        loc.getWorld().dropItem(loc, item);
        return;
    }

    public static ItemStack setItem(String id, int amount) {

        // TODO: Multiple Material Types
        ItemStack item = new ItemStack(Material.OAK_PLANKS,amount);

        /* Item Attributes */
        ItemMeta meta = item.getItemMeta();

        // Set Item Custom Model Data, if it exists, otherwise set to normal Custom Model Data.
        int cmd = ConfigHelper.get(id,"item-custommodeldata",ReturnType.INTEGER);
        if(cmd==0) {
            cmd = ConfigHelper.get(id,"custommodeldata",ReturnType.INTEGER);
        }

        meta.setCustomModelData(cmd);

        // Lore (Optional check)

        if(!main.getConfig().getStringList("Furniture." + id + ".lore").isEmpty()) {
            ArrayList<String> loresList = new ArrayList<String>();
            for(String text : main.getConfig().getStringList("Furniture." + id + ".lore")) {
                loresList.add(ColorUtils.setColor(text));
            }
            meta.setLore(loresList);
        }

        item.setItemMeta(meta);

        String display = main.getConfig().getString("Furniture." + id + ".display");

        if(!main.getConfig().getBoolean("Options.use-translatable-text")) {
            display = ColorUtils.setColor(display);
            meta.setDisplayName(display);

            item.setItemMeta(meta);
        } else {
            item = setTranslateName(item, display);
        }

        return item;
    }

    public static ItemStack setFrameItem(Material material, int customModelData){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setTranslateName(ItemStack item, String name) {
        NBTItem nbti = new NBTItem(item);
        NBTCompound compound = nbti.getOrCreateCompound("display");
        compound.setString("Name", "{\"italic\":false,\"translate\":\"" + name + "\"}");

        return nbti.getItem();
    }
}
