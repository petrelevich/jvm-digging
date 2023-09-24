package com.datasrc;

import com.datasrc.config.ReactiveReceiver;
import com.datasrc.model.StringValue;
import java.util.concurrent.CancellationException;
import java.util.function.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Hooks;

public class StringValueConsumer {
    private static final Logger log = LoggerFactory.getLogger(StringValueConsumer.class);

    private final ConnectableFlux<ConsumerRecord<Long, StringValue>> kafkaFlow;
    private final Disposable kafkaSubscriber;
    private Disposable kafkaConnection;

    public StringValueConsumer(ReactiveReceiver reactiveReceiver, Consumer<StringValue> dataConsumer) {
        Hooks.onErrorDropped(error -> {
            if (error instanceof CancellationException) {
                log.info("Cancellation event:", error);
            } else {
                log.error("error:", error);
            }
        });

        kafkaFlow = reactiveReceiver
                .getInboundFlux()
                .doOnCancel(() -> log.info("connection canceled"))
                .doOnError(error -> log.error("Consuming error", error))
                .publish();

        kafkaSubscriber = kafkaFlow.subscribe(receiverRecord -> {
            var key = receiverRecord.key();
            var value = receiverRecord.value();
            log.info("key:{}, value:{}, record:{}", key, value, receiverRecord);
            dataConsumer.accept(value);
        });
    }

    public void startConsume() {
        log.info("start consuming");
        kafkaConnection = kafkaFlow.connect();
    }

    public void stopConsume() {
        log.info("stop consuming");
        kafkaSubscriber.dispose();
        kafkaConnection.dispose();
    }
}
