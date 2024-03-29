package me.steinborn.krypton.network.util;

/**
 * Credits to the Andrew Steinborn for the original code (from Krypton): https://github.com/astei/krypton
 * No changes made beyond changing mappings.
 */

import io.netty.handler.codec.DecoderException;

/**
 * A special-purpose exception thrown when we want to indicate an error decoding but do not want
 * to see a large stack trace in logs.
 */
public class QuietDecoderException extends DecoderException {

    public QuietDecoderException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
