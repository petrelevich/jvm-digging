package ru.demo.mainpackage.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncActionAnnotated implements AsyncAction {
    private static final Logger logger = LoggerFactory.getLogger(AsyncActionAnnotated.class);

    @Override
    @Async
    public void sayHelloAsync() {
        var isVirtual = Thread.currentThread().isVirtual();
        logger.info("hello, isVirtual:{}", isVirtual);
    }
}
