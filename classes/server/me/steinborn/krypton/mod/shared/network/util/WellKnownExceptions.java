package me.steinborn.krypton.mod.shared.network.util;

/**
 * Credits to the Andrew Steinborn for the original code (from Krypton): https://github.com/astei/krypton
 * No changes have been made.
 */

public enum WellKnownExceptions {
    ;

    public static final QuietDecoderException BAD_LENGTH_CACHED = new QuietDecoderException("Bad packet length");
    public static final QuietDecoderException VARINT_BIG_CACHED = new QuietDecoderException("VarInt too big");
}
