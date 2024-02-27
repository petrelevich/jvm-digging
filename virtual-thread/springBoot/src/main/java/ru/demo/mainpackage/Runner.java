package ru.demo.mainpackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.demo.mainpackage.services.AsyncAction;

@Component
public class Runner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);
    private final AsyncAction asyncAction;

    public Runner(AsyncAction asyncAction) {
        this.asyncAction = asyncAction;
    }

    @Override
    public void run(String... args) {
        logger.info("run begin");
        asyncAction.sayHelloAsync();
        logger.info("run end");
    }

    @Scheduled(fixedDelay = 3_000)
    public void sayHello() {
        var isVirtual = Thread.currentThread().isVirtual();
        logger.info("hello, isVirtual:{}", isVirtual);
    }
}
