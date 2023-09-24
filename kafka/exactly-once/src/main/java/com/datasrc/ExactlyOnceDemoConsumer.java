package com.datasrc;

import com.datasrc.config.ConsumerManualCommit;
import com.datasrc.deduplicator.DeDuplicatorDb;
import com.datasrc.deduplicator.DriverManagerDataSource;
import com.datasrc.deduplicator.StringValueProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExactlyOnceDemoConsumer {
    private static final Logger log = LoggerFactory.getLogger(ExactlyOnceDemoConsumer.class);
    private static final String URL = "jdbc:postgresql://localhost:5430/transactionsDb";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    @SuppressWarnings("java:S125")
    public static void main(String[] args) {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        var deDuplicator = new DeDuplicatorDb(dataSource);

        StringValueProcessor postAction = value -> log.info("value:{}", value);

        // случай 1. Ошибка во время обработки
        // StringValueProcessor postActionError = value -> System.exit(-1);

        var consumer = new ConsumerManualCommit("localhost:9092");
        var dataConsumer = new StringValueConsumer(consumer, deDuplicator, postAction);
        dataConsumer.startSending();

        var shutdownHook = new Thread(() -> {
            consumer.close();
            dataConsumer.stopPulling();
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}
