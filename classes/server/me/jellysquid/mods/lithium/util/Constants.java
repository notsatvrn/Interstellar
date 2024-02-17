package me.jellysquid.mods.lithium.util;

/**
 * Credits to the CaffeineMC team for the original code (from Lithium): https://github.com/CaffeineMC/lithium-fabric
 * A number of new constants have been added.
 */

/**
 * Pre-initialized constants to avoid unnecessary allocations.
 */
public final class Constants {
    public static final byte[] BYTE_EMPTY = new byte[0];

    public static final int[] INT_EMPTY = new int[0]; // Lithium: ArrayConstants.java
    public static final int[] INT_ZERO = new int[]{0}; // Lithium: ArrayConstants.java
    public static final int[] INT_ONE = new int[]{1};
    public static final int[] INT_TWO = new int[]{2};

    public static final long[] LONG_EMPTY = new long[0];

    public static final String[] STRING_EMPTY = new String[0];
    public static final Object[] OBJECT_EMPTY = new Object[0];
    public static final org.bukkit.entity.Entity[] BUKKIT_ENTITY_EMPTY = new org.bukkit.entity.Entity[0];
    public static final net.minecraft.world.entity.Entity[] ENTITY_EMPTY = new net.minecraft.world.entity.Entity[0];
    public static final java.nio.file.LinkOption[] LINKOPTION_EMPTY = new java.nio.file.LinkOption[0];
}
