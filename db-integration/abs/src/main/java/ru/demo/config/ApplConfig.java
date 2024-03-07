package ru.demo.config;

import java.math.BigDecimal;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.demo.model.Payment;
import ru.demo.services.PaymentCreator;
import ru.demo.services.PaymentCreatorDb;

@Configuration
@EnableScheduling
public class ApplConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApplConfig.class);

    @Bean(destroyMethod = "shutdownNow")
    public ScheduledThreadPoolExecutor scheduledExecutor() {
        return new ScheduledThreadPoolExecutor(1);
    }

    @Bean
    public PaymentCreator paymentCreator(DataSource dataSource, ScheduledThreadPoolExecutor scheduledExecutor) {
        var paymentCreator = new PaymentCreatorDb(dataSource);
        AtomicReference<BigDecimal> value = new AtomicReference<>(BigDecimal.valueOf(12.34));
        var step = BigDecimal.valueOf(0.1);
        scheduledExecutor.scheduleAtFixedRate(
                () -> {
                    var paymentId = paymentCreator.create(new Payment("acc1", value.get()));
                    logger.info("paymentId:{}", paymentId);
                    value.set(value.get().add(step));
                },
                1,
                1,
                TimeUnit.SECONDS);
        return paymentCreator;
    }
}
