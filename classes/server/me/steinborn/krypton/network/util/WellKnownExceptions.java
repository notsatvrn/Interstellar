package me.steinborn.krypton.network.util;

/**
 * Credits to the Andrew Steinborn for the original code (from Krypton): https://github.com/astei/krypton
 * No changes made beyond changing mappings.
 */

public enum WellKnownExceptions {
    ;

    public static final QuietDecoderException BAD_LENGTH_CACHED = new QuietDecoderException("Bad packet length");
    public static final QuietDecoderException VARINT_BIG_CACHED = new QuietDecoderException("VarInt too big");
}
