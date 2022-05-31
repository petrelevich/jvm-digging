package ru.code;

import static ru.code.MicrowaveFuncs.loop;

public class MicrowaveDemo {
    public static void main(String[] args) {
        MicrowaveF microwave = new MicrowaveF();
        System.out.println("init:" + microwave.getPowerLevel());

        MicrowaveF microwaveUp = loop(10, microwave, MicrowaveFuncs::powerLevelUp);
        System.out.println("after up:" + microwaveUp.getPowerLevel());

        MicrowaveF microwaveDown = loop(10, microwaveUp, MicrowaveFuncs::powerLevelDown);
        System.out.println("after down:" + microwaveDown.getPowerLevel());
    }
}
