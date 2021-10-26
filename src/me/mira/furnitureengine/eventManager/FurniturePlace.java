package me.mira.furnitureengine.eventManager;

import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

import me.mira.furnitureengine.Main;
import me.mira.furnitureengine.Util;
import me.mira.furnitureengine.events.FurniturePlaceEvent;

public class FurniturePlace implements Listener {
	Main main = Main.getPlugin(Main.class);
	ItemStack itemFrameItem;
	public String id;
	
	public FurniturePlace(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		Block blockPlaced = e.getBlockPlaced();
		ItemStack item = player.getInventory().getItemInMainHand();
		ItemMeta meta = item.getItemMeta();
		// Checking if placed block is custom furniture
		if(main.wg!=null) {
			Location location = new Location(blockPlaced.getWorld(),blockPlaced.getX(),blockPlaced.getY(),blockPlaced.getZ());
			@SuppressWarnings("static-access")
			LocalPlayer localPlayer = main.wg.inst().wrapPlayer(player);
			RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
			RegionQuery query = container.createQuery();
			if(item.getType()==Material.OAK_PLANKS&&(player.hasPermission("furnitureengine.blockplace")||main.getConfig().getBoolean("Options.check-place-permissions")==false)&&(query.testBuild(BukkitAdapter.adapt(location), localPlayer, Flags.BLOCK_PLACE)||player.hasPermission("furnitureengine.admin"))) {
				main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
					String test = main.getConfig().getString("Furniture." + key+".display");
					if(meta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', test))){
						FurniturePlaceEvent event = new FurniturePlaceEvent(player, blockPlaced.getLocation());
						Bukkit.getServer().getPluginManager().callEvent(event);
						if(!event.isCancelled()) {
							if(blockPlaced.getLocation().add(0,1,0).getBlock().getType()==Material.AIR||blockPlaced.getLocation().add(0,1,0).getBlock().getType()==Material.VOID_AIR||blockPlaced.getLocation().add(0,1,0).getBlock().getType()==Material.CAVE_AIR||blockPlaced.getLocation().add(0,1,0).getBlock().getType()==Material.TORCH||blockPlaced.getLocation().add(0,1,0).getBlock().getType()==Material.SOUL_TORCH) {	
								blockPlaced.setType(Material.BARRIER);
								
								// Placing item frame on top of block
								World world = blockPlaced.getWorld();
								ItemFrame frame = (ItemFrame) world.spawn(blockPlaced.getLocation().add(0, 1, 0),ItemFrame.class);
								frame.setInvulnerable(true);
								frame.setFixed(true);
								frame.setVisible(false);
								frame.setItem(Util.setItem(test));
								frame.setFacingDirection(BlockFace.UP);
								
								// Decides what direction the item frame item should be facing
								 float y = player.getLocation().getYaw();
						         if( y < 0 ) y += 360;
						         y %= 360;
						         int i = (int)((y+8) / 22.5);
						         
						         if(main.getConfig().getBoolean("Furniture." + key +".full-rotate")==true) {
						        	// 8 Side Rotation
						        	 // West
						        	 if(i==15||i==0||i==1||i==16) frame.setRotation(Rotation.FLIPPED);
						        	 // North-West
						        	 if(i==2) frame.setRotation(Rotation.FLIPPED_45);
						        	 // North
						        	 if(i==3||i==4||i==5) frame.setRotation(Rotation.COUNTER_CLOCKWISE);
						        	 // North-East
						        	 if(i==6) frame.setRotation(Rotation.COUNTER_CLOCKWISE_45);
						        	 // South-East
						        	 if(i==10) frame.setRotation(Rotation.CLOCKWISE_45);
						        	 // South
						        	 if(i==11||i==12||i==13) frame.setRotation(Rotation.CLOCKWISE);
						        	 // South-West
						        	 if(i==14) frame.setRotation(Rotation.CLOCKWISE_135);
						        	 // East
						        	 if(i==7||i==8||i==9) frame.setRotation(Rotation.NONE);
						        	 
						        
						         } else {
						        	// 4 Side Rotation
							         if(i==6||i==7||i==8||i==9) frame.setRotation(Rotation.NONE);
							         if(i==10||i==11||i==12||i==13||i==14) frame.setRotation(Rotation.CLOCKWISE);
							         if(i==2||i==3||i==4||i==5) frame.setRotation(Rotation.COUNTER_CLOCKWISE);
							         if(i==15||i==16||i==0||i==1) frame.setRotation(Rotation.FLIPPED);
						         }
								
									} else {
										e.setCancelled(true);
									}
						} else e.setCancelled(true);
						return;
				}
				});
			} else if(item.getType()==Material.OAK_PLANKS&&!(player.hasPermission("furnitureengine.blockplace")&&main.getConfig().getBoolean("Options.check-place-permissions")==true)){
				if(item.getItemMeta().hasCustomModelData()) {
					e.setCancelled(true);
				}
			}
		} else
		if(item.getType()==Material.OAK_PLANKS&&(player.hasPermission("furnitureengine.blockplace")||main.getConfig().getBoolean("Options.check-place-permissions")==false)) {
			main.getConfig().getConfigurationSection("Furniture").getKeys(false).forEach(key -> {
				String test = main.getConfig().getString("Furniture." + key+".display");
				if(meta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', test))){
					FurniturePlaceEvent event = new FurniturePlaceEvent(player, blockPlaced.getLocation());
					Bukkit.getServer().getPluginManager().callEvent(event);
					if(!event.isCancelled()) {
						if(blockPlaced.getLocation().add(0,1,0).getBlock().getType()==Material.AIR||blockPlaced.getLocation().add(0,1,0).getBlock().getType()==Material.CAVE_AIR||blockPlaced.getLocation().add(0,1,0).getBlock().getType()==Material.TORCH||blockPlaced.getLocation().add(0,1,0).getBlock().getType()==Material.SOUL_TORCH) {	
							blockPlaced.setType(Material.BARRIER);
							
							// Placing item frame on top of block
							World world = blockPlaced.getWorld();
							ItemFrame frame = (ItemFrame) world.spawn(blockPlaced.getLocation().add(0, 1, 0),ItemFrame.class);
							frame.setInvulnerable(true);
							frame.setFixed(true);
							frame.setVisible(false);
							frame.setItem(Util.setItem(test));
							frame.setFacingDirection(BlockFace.UP);
							
							// Decides what direction the item frame item should be facing
							 float y = player.getLocation().getYaw();
					         if( y < 0 ) y += 360;
					         y %= 360;
					         int i = (int)((y+8) / 22.5);
					         
					         if(main.getConfig().getBoolean("Furniture." + key +".full-rotate")==true) {
						        	// 8 Side Rotation
						        	 // West
						        	 if(i==15||i==0||i==1||i==16) frame.setRotation(Rotation.FLIPPED);
						        	 // North-West
						        	 if(i==2) frame.setRotation(Rotation.FLIPPED_45);
						        	 // North
						        	 if(i==3||i==4||i==5) frame.setRotation(Rotation.COUNTER_CLOCKWISE);
						        	 // North-East
						        	 if(i==6) frame.setRotation(Rotation.COUNTER_CLOCKWISE_45);
						        	 // South-East
						        	 if(i==10) frame.setRotation(Rotation.CLOCKWISE_45);
						        	 // South
						        	 if(i==11||i==12||i==13) frame.setRotation(Rotation.CLOCKWISE);
						        	 // South-West
						        	 if(i==14) frame.setRotation(Rotation.CLOCKWISE_135);
						        	 // East
						        	 if(i==7||i==8||i==9) frame.setRotation(Rotation.NONE);
						        	 
						        
						         } else {
						        	// 4 Side Rotation
							         if(i==6||i==7||i==8||i==9) frame.setRotation(Rotation.NONE);
							         if(i==10||i==11||i==12||i==13||i==14) frame.setRotation(Rotation.CLOCKWISE);
							         if(i==2||i==3||i==4||i==5) frame.setRotation(Rotation.COUNTER_CLOCKWISE);
							         if(i==15||i==16||i==0||i==1) frame.setRotation(Rotation.FLIPPED);
						         }
							
								} else e.setCancelled(true);
					} else e.setCancelled(true);
					return;
			}
			});
		} else if(item.getType()==Material.OAK_PLANKS&&!(player.hasPermission("furnitureengine.blockplace")&&main.getConfig().getBoolean("Options.check-place-permissions")==true)){
			if(item.getItemMeta().hasCustomModelData()) {
				e.setCancelled(true);
			}
			return;
		}
	}
}
