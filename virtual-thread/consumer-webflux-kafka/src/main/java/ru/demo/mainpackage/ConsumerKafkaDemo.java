package ru.demo.mainpackage;

import io.micrometer.context.ContextRegistry;
import io.micrometer.context.integration.Slf4jThreadLocalAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class ConsumerKafkaDemo {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerKafkaDemo.class);

    public static void main(String[] args) {
        ContextRegistry.getInstance().registerThreadLocalAccessor(new Slf4jThreadLocalAccessor());
        Hooks.enableAutomaticContextPropagation();

        com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)
                java.lang.management.ManagementFactory.getOperatingSystemMXBean();

        logger.info("pid:{}", ProcessHandle.current().pid());
        logger.info("availableProcessors:{}", Runtime.getRuntime().availableProcessors());
        logger.info("TotalMemorySize, mb:{}", os.getTotalMemorySize() / 1024 / 1024);
        logger.info("maxMemory, mb:{}", Runtime.getRuntime().maxMemory() / 1024 / 1024);
        logger.info("freeMemory, mb:{}", Runtime.getRuntime().freeMemory() / 1024 / 1024);

        SpringApplication.run(ConsumerKafkaDemo.class, args);
    }
}
