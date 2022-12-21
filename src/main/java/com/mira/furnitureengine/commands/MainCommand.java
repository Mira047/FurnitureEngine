package com.mira.furnitureengine.commands;

import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {
    FurnitureEngine main = FurnitureEngine.getPlugin(FurnitureEngine.class);

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if(args.length==0) {
            sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Incorrect Command usage!");

            return true;
        }
        else {
            /* Reload */
            if(args[0].equals("reload")) {
                reloadCommand(sender);
                return true;
            }
            /* Give */
            if(args[0].equals("give")) {
                if(args.length==1) {
                    sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Incorrect Command usage!");
                    return false;
                } else {
                    main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
                        if(args[2].equals(key)) {
                            if(args.length==4&&Integer.parseInt(args[3])!=0) {
                                ItemUtils.giveItem(Bukkit.getPlayer(args[1]),key,Integer.parseInt(args[3]),null);
                            } else ItemUtils.giveItem(Bukkit.getPlayer(args[1]),key, 1, null);
                        }
                    });

                }

                return true;
            }
            if(args[0].equals("get")) {
                if(args.length==1) {
                    sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Incorrect Command usage!");
                    return false;
                } else {
                    if(sender instanceof Player) {
                        main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
                            if(args[1].equals(key)) {
                                if(args.length==3&&Integer.parseInt(args[2])!=0) {
                                    ItemUtils.giveItem((Player) sender,key,Integer.parseInt(args[2]),null);
                                } else ItemUtils.giveItem((Player) sender,key, 1, null);
                            }
                        });
                    }
                }

                return true;
            }
        }
        return false;
    }

    public void reloadCommand(CommandSender sender) {
        main.reloadConfig();
        sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Config reloaded!");
    }
}
