package ru.logs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/*
<ArrayBlockingQueue/>
<DisruptorBlockingQueue/>

https://martinfowler.com/articles/lmax.html
https://github.com/LMAX-Exchange/disruptor/wiki/Introduction
https://lmax-exchange.github.io/disruptor/files/Disruptor-1.0.pdf
 */

public class LoggerTest {
    private static final Logger logger = LogManager.getLogger(LoggerTest.class);
    private static final Logger loggerSlow = LogManager.getLogger(LoggerTest.class.getName() + ".slow");
    private static final Logger loggerSlowAsync = LogManager.getLogger(LoggerTest.class.getName() + ".slowAsync");

    public static void main(String[] args) {
     //   someActionWithLog(loggerSlow);
        System.out.println("----------------");
        someActionWithLog(loggerSlowAsync);
    }

    private static void someActionWithLog(Logger loggerExperimental) {
        for (int idx = 0; idx < 1_000; idx++) {
            logger.info("action before:{}", idx);
            loggerExperimental.info("logging in slow:{}", idx);
            logger.info("action after:{}", idx);

            sleep();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
