package ru.demo.mainpackage.services;

import static ru.demo.mainpackage.model.PaymentStatus.DONE;
import static ru.demo.mainpackage.model.PaymentStatus.ERROR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.mainpackage.model.Payment;
import ru.demo.mainpackage.repository.PaymentRepository;
import ru.demo.mainpackage.transactions.TransactionRunner;

public class PaymentProcessorDb implements PaymentProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PaymentProcessorDb.class);

    private final PaymentRepository paymentRepository;
    private final TransactionRunner transactionRunner;

    public PaymentProcessorDb(PaymentRepository paymentRepository, TransactionRunner transactionRunner) {
        this.paymentRepository = paymentRepository;
        this.transactionRunner = transactionRunner;
    }

    @Override
    public void process() {
        try {
            transactionRunner.doInTransaction(() -> {
                var payments = paymentRepository.findNewPayments();
                logger.info("payments for process:{}", payments.size());
                for (var payment : payments) {
                    process(payment);
                }
            });
        } catch (Exception ex) {
            logger.info("payment process error", ex);
        }
    }

    private void process(Payment payment) {
        logger.info("start process payment:{}", payment);
        try {
            sleep();
            paymentRepository.save(payment.toBuilder().status(DONE).build());
            logger.info("done process payment:{}", payment);
        } catch (Exception ex) {
            logger.error("error process payment:{}", payment, ex);
            paymentRepository.save(payment.toBuilder().status(ERROR).build());
        }
    }

    private void sleep() {
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
