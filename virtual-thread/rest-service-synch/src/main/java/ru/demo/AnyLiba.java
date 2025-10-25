package ru.demo;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnyLiba {
    private static final Logger log = LoggerFactory.getLogger(AnyLiba.class);

    @SuppressWarnings("java:S2276")
    public String action() throws InterruptedException {
        synchronized (this) {
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));
            log.info("action done");
        }
        return "Ok";
    }
}
