package ru.demo.threadlocal;

import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLocalDemo {
    private static final Logger log = LoggerFactory.getLogger(ThreadLocalDemo.class);

    private ThreadLocalDemo() {}

    static void main() throws InterruptedException {
        var threadLocal = new ThreadLocal<Integer>();
        threadLocal.set(1);
        log.info("threadLocal_1:{}", threadLocal.get());

        var t10 = new Thread(
                () -> {
                    log.info("threadLocal_10:{}", threadLocal.get());
                    threadLocal.set(10);
                    log.info("threadLocal_10:{}", threadLocal.get());
                },
                "t-10");
        t10.start();

        var t20 = new Thread(
                () -> {
                    log.info("threadLocal_20:{}", threadLocal.get());
                    threadLocal.set(20);
                    log.info("threadLocal_20:{}", threadLocal.get());
                },
                "t-20");
        t20.start();

        t10.join();
        t20.join();

        log.info("-----------------");
        var request = new ThreadLocal<Integer>();
        try (var executor = Executors.newSingleThreadExecutor()) {
            executor.submit(() -> {
                try {
                    request.set(33);
                    log.info("task_33:{}", request.get());
                } finally {
                    request.remove();
                }
            });

            executor.submit(() -> log.info("task_44:{}", request.get()));
        }
    }
}
