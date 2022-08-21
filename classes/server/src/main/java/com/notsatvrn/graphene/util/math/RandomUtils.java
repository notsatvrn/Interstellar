// Original code by RelativityMC, licensed under MIT
// You can find the original code on https://github.com/RelativityMC/C2ME-fabric (Yarn mappings)

package com.notsatvrn.graphene.util;

import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.Xoroshiro128PlusPlus;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

public class RandomUtils {
    private static final ThreadLocal<XoroshiroRandomSource> xoroshiro = ThreadLocal.withInitial(() -> new XoroshiroRandomSource(0L, 0L));
    private static final ThreadLocal<SingleThreadedRandomSource> simple = ThreadLocal.withInitial(() -> new SingleThreadedRandomSource(0L));

    public static void derive(PositionalRandomFactory deriver, RandomSource random, int x, int y, int z) {
        if (deriver instanceof XoroshiroRandomSource.XoroshiroPositionalRandomFactory deriver1) {
            final Xoroshiro128PlusPlus implementation = ((XoroshiroRandomSource) random).getRandomNumberGenerator();
            implementation.setSeedLo(Mth.getSeed(x, y, z) ^ deriver1.getSeedLo());
            implementation.setSeedHi(deriver1.getSeedHi());
            return;
        }
        if (deriver instanceof LegacyRandomSource.LegacyPositionalRandomFactory deriver1) {
            final SingleThreadedRandomSource random1 = (SingleThreadedRandomSource) random;
            random1.setSeed(Mth.getSeed(x, y, z) ^ deriver1.getSeed());
            return;
        }
        throw new IllegalArgumentException();
    }

    public static RandomSource getThreadLocalRandom(PositionalRandomFactory deriver) {
        if (deriver instanceof XoroshiroRandomSource.XoroshiroPositionalRandomFactory) {
            return xoroshiro.get();
        }
        if (deriver instanceof LegacyRandomSource.LegacyPositionalRandomFactory) {
            return simple.get();
        }
        throw new IllegalArgumentException();
    }

    public static RandomSource getRandom(PositionalRandomFactory deriver) {
        if (deriver instanceof XoroshiroRandomSource.XoroshiroPositionalRandomFactory) {
            return new XoroshiroRandomSource(0L, 0L);
        }
        if (deriver instanceof LegacyRandomSource.LegacyPositionalRandomFactory) {
            return new SingleThreadedRandomSource(0L);
        }
        throw new IllegalArgumentException();
    }
}

