package ru.demo.mainpackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Counter {
    private static final Logger log = LoggerFactory.getLogger(Counter.class);

    public static void main(String[] args) {
        var os = (com.sun.management.OperatingSystemMXBean)
                java.lang.management.ManagementFactory.getOperatingSystemMXBean();

        log.info("pid:{}", ProcessHandle.current().pid());
        log.info("availableProcessors:{}", Runtime.getRuntime().availableProcessors());
        log.info("TotalMemorySize, mb:{}", os.getTotalMemorySize() / 1024 / 1024);
        log.info("maxMemory, mb:{}", Runtime.getRuntime().maxMemory() / 1024 / 1024);
        log.info("freeMemory, mb:{}", Runtime.getRuntime().freeMemory() / 1024 / 1024);

        SpringApplication.run(Counter.class, args);
    }
}
