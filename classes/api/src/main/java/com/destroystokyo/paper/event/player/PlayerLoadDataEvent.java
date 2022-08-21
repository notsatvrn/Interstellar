package com.destroystokyo.paper.event.player;

import com.google.gson.JsonObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Calls an event in which playerdata can be provided. If null, will load from disk, otherwise will use provided data
 */
public class PlayerLoadDataEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final UUID playerId;
    private Object playerData;
    private JsonObject statistics;

    public PlayerLoadDataEvent(@NotNull UUID playerId) {
        super();
        this.playerId = playerId;
    }

    /**
     * Gets the player's unique ID.
     *
     * @return The unique ID
     */
    @NotNull
    public UUID getUniqueId() {
        return playerId;
    }

    @Nullable
    public Object getPlayerData() {
        return playerData;
    }

    public void setPlayerData(@NotNull Object playerData) {
        this.playerData = playerData;
    }

    @Nullable
    public JsonObject getStatistics() {
        return statistics;
    }

    public void setStatistics(@NotNull JsonObject statistics) {
        this.statistics = statistics;
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

