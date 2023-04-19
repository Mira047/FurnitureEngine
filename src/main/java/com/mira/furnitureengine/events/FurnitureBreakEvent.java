package com.mira.furnitureengine.events;

import com.mira.furnitureengine.furniture.core.Furniture;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FurnitureBreakEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private boolean isDroppingItems;

    private final Furniture furniture;

    private final Player interactingPlayer;

    private final Location furnitureLocation;

    public FurnitureBreakEvent(Furniture furniture, Player interactingPlayer, Location furnitureLocation) {
        this.furniture = furniture;
        this.interactingPlayer = interactingPlayer;
        this.furnitureLocation = furnitureLocation;

        isDroppingItems = true;
    }

    public Player getPlayer() {
        return interactingPlayer;
    }

    public Location getFurnitureLocation() {
        return furnitureLocation;
    }

    public Furniture getFurniture() {
        return furniture;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public boolean isDroppingItems() {
        return isDroppingItems;
    }

    public void setDroppingItems(boolean isDroppingItems) {
        this.isDroppingItems = isDroppingItems;
    }

}