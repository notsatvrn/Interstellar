package me.satvrn.random;

import java.util.Random;

import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.Xoroshiro128PlusPlus;

public class FastRandom extends Random {
    private Xoroshiro128PlusPlus source;

    public FastRandom() {
        super();
    }

    public FastRandom(long seed) {
        super(seed);
    }

    @Override
    public long nextLong() {
        return this.source.nextLong();
    }

    @Override
    public void setSeed(long seed) {
        this.source = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(seed));
    }
}
