package ru.demo.mainpackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;
import io.micrometer.context.ContextRegistry;
import ru.demo.mainpackage.model.MdcField;

@SpringBootApplication
public class ConsumerKafkaDemo {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerKafkaDemo.class);
    public static final MdcField MDC_FIELD_REQUEST_ID = new MdcField("requestId");

    public static void main(String[] args) {
        com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)
                java.lang.management.ManagementFactory.getOperatingSystemMXBean();

        logger.info("pid:{}", ProcessHandle.current().pid());
        logger.info("availableProcessors:{}", Runtime.getRuntime().availableProcessors());
        logger.info("TotalMemorySize, mb:{}", os.getTotalMemorySize() / 1024 / 1024);
        logger.info("maxMemory, mb:{}", Runtime.getRuntime().maxMemory() / 1024 / 1024);
        logger.info("freeMemory, mb:{}", Runtime.getRuntime().freeMemory() / 1024 / 1024);

        Hooks.enableAutomaticContextPropagation();
        initMDC();
        SpringApplication.run(ConsumerKafkaDemo.class, args);
    }

    private static void initMDC() {
        ContextRegistry.getInstance().registerThreadLocalAccessor(
                MDC_FIELD_REQUEST_ID.name(),
                () -> MDC.get(MDC_FIELD_REQUEST_ID.name()),
                value -> MDC.put(MDC_FIELD_REQUEST_ID.name(), String.valueOf(value)),
                () -> MDC.remove(MDC_FIELD_REQUEST_ID.name()));
    }
}
