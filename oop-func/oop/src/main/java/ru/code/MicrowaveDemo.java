package ru.code;

public class MicrowaveDemo {
    public static void main(String[] args) {
        MicrowaveO microwave = new MicrowaveO();
        System.out.println("init:" + microwave.getPowerLevel());

        for (int idx = 0; idx < 10; idx++) {
            microwave.powerLevelUp();
        }
        System.out.println("after up:" + microwave.getPowerLevel());

        for (int idx = 0; idx < 10; idx++) {
            microwave.powerLevelDown();
        }
        System.out.println("after down:" + microwave.getPowerLevel());
    }
}
