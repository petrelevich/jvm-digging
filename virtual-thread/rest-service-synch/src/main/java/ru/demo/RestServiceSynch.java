package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RestServiceSynch {
    private static final Logger log = LoggerFactory.getLogger(RestServiceSynch.class);

    public static void main(String[] args) {
        com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)
                java.lang.management.ManagementFactory.getOperatingSystemMXBean();

        log.info("availableProcessors:{}", Runtime.getRuntime().availableProcessors());
        log.info("TotalMemorySize, mb:{}", os.getTotalMemorySize() / 1024 / 1024);
        log.info("maxMemory, mb:{}", Runtime.getRuntime().maxMemory() / 1024 / 1024);
        log.info("freeMemory, mb:{}", Runtime.getRuntime().freeMemory() / 1024 / 1024);

        new SpringApplicationBuilder().sources(RestServiceSynch.class).run(args);
    }
}
