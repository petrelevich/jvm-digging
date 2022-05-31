package ru.code;

public class MicrowaveO {
    private int powerLevel = 10;

    public void powerLevelUp() {
        powerLevel++;
    }

    public void powerLevelDown() {
        powerLevel--;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void wifiCommand(byte[] cmdAsByte) {
        String cmd = new String(cmdAsByte);
        switch (cmd) {
            case "up" -> powerLevelUp();
            case "down" -> powerLevelDown();
            default -> throw new RuntimeException("unknown cmd:" + cmd);
        }
    }
}
