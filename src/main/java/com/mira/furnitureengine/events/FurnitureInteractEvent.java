package com.mira.furnitureengine.events;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.NotNull;

public class FurnitureInteractEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private final Player interactingPlayer;
    private final Location furnitureLocation;
    private final BlockFace blockFace;
    private final Action action;

    public FurnitureInteractEvent(Player interactingPlayer, Action action, Location furnitureLocation, BlockFace blockFace) {
        this.interactingPlayer = interactingPlayer;
        this.furnitureLocation = furnitureLocation;
        this.blockFace = blockFace;
        this.action = action;
    }

    public Player getPlayer() {
        return interactingPlayer;
    }

    public Location getFurnitureLocation() {
        return furnitureLocation;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public Action getAction() {
        return action;
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
}