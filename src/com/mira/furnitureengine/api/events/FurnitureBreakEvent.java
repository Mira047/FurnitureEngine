package com.mira.furnitureengine.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FurnitureBreakEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private boolean dropitem;
	
	private Player interactingPlayer; 
	
	private Location furnitureLocation;
	
	public FurnitureBreakEvent(Player interactingPlayer, Location furnitureLocation) {
		this.interactingPlayer = interactingPlayer;
		this.furnitureLocation = furnitureLocation;
		
		// Drops items by default
		dropItems(true);
	}
	
	public Player getPlayer() {
		return interactingPlayer;
	}
	
	public Location getFurnitureLocation() {
		return furnitureLocation;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public boolean isDroppingItems() {
		return dropitem;
	}
	
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
	public void dropItems(boolean yn) {
		dropitem = yn;
	}

	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}

}
