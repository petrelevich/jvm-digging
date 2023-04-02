package ru.demo;

import java.util.concurrent.TimeUnit;

public class SlowDataSrc {

    private SlowDataSrc() {}

    public static long getValue(int key) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return key;
    }
}
