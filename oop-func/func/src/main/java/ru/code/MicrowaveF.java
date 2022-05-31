package ru.code;

public class MicrowaveF {
    private final int powerLevel;

    public MicrowaveF() {
        powerLevel = 10;
    }

    public MicrowaveF(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public int getPowerLevel() {
        return powerLevel;
    }
}
