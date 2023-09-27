package me.satvrn.random;

import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;

public class XorwowRandom extends Random {
    static class RandomHelper {
        static final long multiplier = 0x5DEECE66DL;
        static final long mask = (1L << 48) - 1;
        static long seedUniquifier() {
            for (;;) {
                long current = seedUniquifier.get();
                long next = current * 1181783497276652981L;
                if (seedUniquifier.compareAndSet(current, next)) {
                    return next;
                }
            }
        }
        static final AtomicLong seedUniquifier = new AtomicLong(8682522807148012L);
        static long initialScramble(long seed) {
            return (seed ^ multiplier) & mask;
        }
    
        static long createSeed() {
            return initialScramble(seedUniquifier() ^ System.nanoTime());
        }
    }

    public long x = RandomHelper.createSeed();
    public long y = 362436069L;
    public long z = 521288629L;
    public long w = 88675123L;
    public long v = 5783321L;
    public long d = 6615241L;

    public XorwowRandom() {
        super();
    }

    public XorwowRandom(long seed) {
        super(seed);
    }

    @Override
    public long nextLong() {
        long t = (x ^ (x >> 2));
        x = y;
        y = z;
        z = w;
        w = v;
        v = (v ^ (v << 4)) ^ (t ^ (t << 1));
        return (d += 362437) + v;
    }

    @Override
    public void setSeed(long seed) {
        x = seed;
        y = 362436069L;
        z = 521288629L;
        w = 88675123L;
        v = 5783321L;
        d = 6615241L;
    }
}