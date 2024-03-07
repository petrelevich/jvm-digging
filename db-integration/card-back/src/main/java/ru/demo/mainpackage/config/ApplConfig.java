package ru.demo.mainpackage.config;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.demo.mainpackage.repository.PaymentRepository;
import ru.demo.mainpackage.services.PaymentProcessor;
import ru.demo.mainpackage.services.PaymentProcessorDb;
import ru.demo.mainpackage.transactions.TransactionRunner;

@Configuration
@EnableScheduling
public class ApplConfig {

    @Bean(destroyMethod = "shutdownNow")
    public ScheduledThreadPoolExecutor scheduledExecutor() {
        return new ScheduledThreadPoolExecutor(1);
    }

    @Bean
    public PaymentProcessor paymentProcessor(
            PaymentRepository paymentRepository,
            TransactionRunner transactionRunner,
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        var paymentProcessor = new PaymentProcessorDb(paymentRepository, transactionRunner);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(paymentProcessor::process, 0, 100, TimeUnit.MILLISECONDS);
        return paymentProcessor;
    }
}
