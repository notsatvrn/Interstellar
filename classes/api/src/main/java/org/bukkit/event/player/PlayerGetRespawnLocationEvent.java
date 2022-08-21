package org.bukkit.event.player;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a respawn event tries to determine the location of a respawn
 */
public class PlayerGetRespawnLocationEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private Location respawnLocation;

    public PlayerGetRespawnLocationEvent(@NotNull final Player respawnPlayer) {
        super(respawnPlayer);
    }

    /**
     * Gets the current respawn location
     *
     * @return Location current respawn location
     */
    @Nullable
    public Location getRespawnLocation() {
        return this.respawnLocation;
    }

    /**
     * Sets the new respawn location
     *
     * @param respawnLocation new location for the respawn
     */
    public void setRespawnLocation(@NotNull Location respawnLocation) {
        Validate.notNull(respawnLocation, "Respawn location can not be null");
        Validate.notNull(respawnLocation.getWorld(), "Respawn world can not be null");

        this.respawnLocation = respawnLocation;
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

