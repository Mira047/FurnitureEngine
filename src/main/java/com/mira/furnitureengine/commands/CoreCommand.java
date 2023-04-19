package com.mira.furnitureengine.commands;

import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.furniture.FurnitureManager;
import com.mira.furnitureengine.furniture.core.Furniture;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CoreCommand implements CommandExecutor {
    FurnitureEngine plugin = FurnitureEngine.getInstance();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if(args.length==0) {
            sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Incorrect Command usage!");

            return true;
        }
        else {
            /* Reload */
            if(args[0].equals("reload")) {
                reload(sender, args.length>=2 ? args[1] : "");

                return true;
            }
            /* Give */
            if(args[0].equals("give")) {
                if(args.length==1) {
                    sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Incorrect Command usage!");
                    return false;
                } else {

                }

                return true;
            }
            if(args[0].equals("get")) {
                if(args.length==1) {
                    sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Incorrect Command usage!");
                    return false;
                } else {
                    if(sender instanceof Player player) {
                        if(args.length == 2) {
                            Furniture furniture = FurnitureManager.getInstance().getFurniture(args[1]);

                            if(furniture != null) {
                                ItemStack item = furniture.getGeneratedItem();

                                if(item != null) {
                                    player.getInventory().addItem(item);
                                    sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GREEN + "Given " + ChatColor.YELLOW + furniture.getId() + ChatColor.GREEN + " to " + ChatColor.YELLOW + player.getName() + ChatColor.GREEN + "!");
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Failed to generate item!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Invalid furniture!");
                            }
                        }
                        else if(args.length == 3) {
                            Furniture furniture = FurnitureManager.getInstance().getFurniture(args[1]);

                            if(furniture != null) {
                                ItemStack item = furniture.getGeneratedItem();

                                if(item != null) {
                                    int amount = Integer.parseInt(args[2]);

                                    if(amount > 0) {
                                        item.setAmount(amount);
                                        player.getInventory().addItem(item);
                                        sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GREEN + "Given " + ChatColor.YELLOW + furniture.getId() + ChatColor.GREEN + " to " + ChatColor.YELLOW + player.getName() + ChatColor.GREEN + "!");
                                    } else {
                                        sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Invalid amount!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Failed to generate item!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Invalid furniture!");
                            }
                        }
                    }
                }

                return true;
            }
        }
        return false;
    }

    public void reload(CommandSender sender, String arg) {
        // No label - Reload all
        // furniture - Reload furniture
        // config - Reload config
        // all - Reload all

        if(arg.isEmpty() || arg.equalsIgnoreCase("all")) {
            plugin.reloadConfig();
            FurnitureManager.getInstance().reloadFurniture();
            sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GREEN + "Reloaded furniture + config!");
        } else if(arg.equalsIgnoreCase("furniture")) {
            FurnitureManager.getInstance().reloadFurniture();
            sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GREEN + "Reloaded furniture!");
        } else if(arg.equalsIgnoreCase("config")) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GREEN + "Reloaded config!");
        } else {
            sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Invalid argument!");
        }
    }
}
