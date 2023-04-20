package com.mira.furnitureengine.commands;

import com.mira.furnitureengine.FurnitureEngine;
import com.mira.furnitureengine.furniture.FurnitureManager;
import com.mira.furnitureengine.furniture.core.Furniture;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
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
                    if(args.length==2) {
                        sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Incorrect Command usage!");
                        return false;
                    }
                    else if(args.length==3) {
                        Player target = plugin.getServer().getPlayer(args[1]);

                        if(target==null) {
                            sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Player not found!");
                            return false;
                        }
                        else {
                            Furniture furniture = FurnitureManager.getInstance().getFurniture(args[2]);

                            if(furniture!=null) {
                                ItemStack item = furniture.getGeneratedItem();

                                if(item!=null) {
                                    if(item.getType()==Material.TIPPED_ARROW) {
                                        PotionMeta meta = ((PotionMeta) item.getItemMeta());
                                        meta.setColor(Color.WHITE);
                                        item.setItemMeta(meta);
                                    }

                                    target.getInventory().addItem(item);
                                    sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GREEN + "Given " + ChatColor.YELLOW + furniture.getId() + ChatColor.GREEN + " to " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + "!");
                                }
                                else {
                                    sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Failed to generate item!");
                                }
                            }
                            else {
                                sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Furniture not found!");
                            }
                        }
                    }
                    else if(args.length==4) {
                        Player target = plugin.getServer().getPlayer(args[1]);

                        if(target==null) {
                            sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Player not found!");
                            return false;
                        }
                        else {
                            Furniture furniture = FurnitureManager.getInstance().getFurniture(args[2]);

                            if(furniture!=null) {
                                ItemStack item = furniture.getGeneratedItem();

                                if(item!=null) {
                                    if(item.getType()==Material.TIPPED_ARROW) {
                                        PotionMeta meta = ((PotionMeta) item.getItemMeta());
                                        meta.setColor(Color.WHITE);
                                        item.setItemMeta(meta);
                                    }

                                    int amount = Integer.parseInt(args[3]);

                                    if(amount>0) {
                                        item.setAmount(amount);
                                        target.getInventory().addItem(item);
                                        sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GREEN + "Given " + ChatColor.YELLOW + furniture.getId() + ChatColor.GREEN + " to " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + "!");
                                    }
                                    else {
                                        sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Invalid amount!");
                                    }
                                }
                                else {
                                    sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Failed to generate item!");
                                }
                            }
                            else {
                                sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Furniture not found!");
                            }
                        }
                    }
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
                                    if(item.getType() == Material.TIPPED_ARROW) {
                                        PotionMeta meta = ((PotionMeta) item.getItemMeta());
                                        meta.setColor(Color.WHITE);
                                        item.setItemMeta(meta);
                                    }

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
                                    if(item.getType() == Material.TIPPED_ARROW) {
                                        PotionMeta meta = ((PotionMeta) item.getItemMeta());
                                        meta.setColor(Color.WHITE);
                                        item.setItemMeta(meta);
                                    }

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
            } else if (args[0].equals("paint")) {
                if(args.length == 1) {
                    sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Incorrect Command usage!");
                    return false;
                }

                ItemStack item = ((Player) sender).getInventory().getItemInMainHand();

                if(!item.hasItemMeta()) {
                    item = ((Player) sender).getInventory().getItemInOffHand();

                    if(!item.hasItemMeta()) {
                        sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "You must hold an item!");
                        return true;
                    }
                }

                if(item.getType() == Material.TIPPED_ARROW) {
                    // Get hex color from the second argument
                    String hex = args[1];

                    PotionMeta meta = (PotionMeta) item.getItemMeta();

                    if(hex.startsWith("#")) {
                        hex = hex.substring(1);
                    } else {
                        sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Invalid hex color!");
                        return true;
                    }

                    if(hex.length() != 6) {
                        sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Invalid hex color!");
                        return true;
                    }

                    try {
                        int r = Integer.parseInt(hex.substring(0, 2), 16);
                        int g = Integer.parseInt(hex.substring(2, 4), 16);
                        int b = Integer.parseInt(hex.substring(4, 6), 16);

                        meta.setColor(Color.fromRGB(r, g, b));
                        item.setItemMeta(meta);

                        ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_DOLPHIN_SPLASH, 1, 1);
                        sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.GREEN + "Painted item!");
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "Invalid hex color!");
                    }
                } else {
                    sender.sendMessage(ChatColor.GOLD + "Furniture" + ChatColor.YELLOW + "Engine" + ChatColor.DARK_GRAY + " » " + ChatColor.RED + "This item is not paintable! (Tipped Arrow)");
                }
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
