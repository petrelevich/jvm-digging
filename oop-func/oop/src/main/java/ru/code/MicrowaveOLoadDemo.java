package ru.code;

//-Xms64m -Xmx64m
public class MicrowaveOLoadDemo {
    public static void main(String[] args) {
        MicrowaveO microwave = new MicrowaveO();
        long start = System.currentTimeMillis();

        for (int counter = 0; counter < 100_000_000; counter++) {
            for (int idx = 0; idx < 10; idx++) {
                microwave.powerLevelUp();
            }
            for (int idx = 0; idx < 10; idx++) {
                microwave.powerLevelDown();
            }
        }
        System.out.println("time:" + (System.currentTimeMillis() - start));
    }
}
