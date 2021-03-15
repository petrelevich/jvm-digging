package ru.systema;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class IntSourceImpl implements IntSource {
    private final Random random = new Random();

    @Override
    public int getInt() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return random.nextInt(100);
    }
}
