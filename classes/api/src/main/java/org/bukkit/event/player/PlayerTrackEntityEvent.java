package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player tracks an entity.
 */
public class PlayerTrackEntityEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Entity tracked;

    public PlayerTrackEntityEvent(@NotNull final Player player, @NotNull final Entity tracked) {
        super(player);
        this.tracked = tracked;
    }

    /**
     * Gets the entity newly tracked by the Player
     *
     * @return Entity the player is now tracking
     */
    @NotNull
    public Entity getTracked() {
        return tracked;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

