package ru.code;

import static ru.code.MicrowaveFuncs.loop;

//-Xms64m -Xmx64m
public class MicrowaveFLoadDemo {
    public static void main(String[] args) {
        MicrowaveF microwave = new MicrowaveF();
        long start = System.currentTimeMillis();

        for (int counter = 0; counter < 100_000_000; counter++) {

            MicrowaveF microwaveUp = loop(10, microwave, MicrowaveFuncs::powerLevelUp);
            microwave = loop(10, microwaveUp, MicrowaveFuncs::powerLevelDown);
        }
        System.out.println("time:" + (System.currentTimeMillis() - start));
    }
}
