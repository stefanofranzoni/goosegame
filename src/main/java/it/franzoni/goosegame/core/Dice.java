package it.franzoni.goosegame.core;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
    private final int maxValue;
    
    public Dice(int maxValue) {
        this.maxValue = maxValue;
    }
    
    public Dice() {
        this(6);
    }

    public int roll() {
        return ThreadLocalRandom.current().nextInt(1, maxValue+1);
    }
    
    public int maxValue() {
        return maxValue;
    }
}
