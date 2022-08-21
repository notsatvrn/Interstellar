package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a block is broken another block. This is generally the result of BlockPhysicsEvent propagation
 */
public class BlockDestroyedByNeighborEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Block sourceBlock;
    private boolean cancel;

    public BlockDestroyedByNeighborEvent(@NotNull final Block theBlock, @Nullable Player player, @NotNull final Block sourceBlock) {
        super(theBlock);

        this.player = player;
        this.sourceBlock = sourceBlock;
    }

    /**
     * Gets the Player that caused this
     *
     * @return The Player that is breaking the block involved in this event
     */
    @Nullable
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the source block that caused this block break
     *
     * @return The Source Block which block is involved in this event
     */
    @NotNull
    public final Block getSourceBlock() {
        return sourceBlock;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

