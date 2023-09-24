package com.datasrc;

import com.datasrc.config.ReactiveSender;
import com.datasrc.model.CorrelationMetadata;
import com.datasrc.model.StringValue;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.SenderRecord;

public class DataSender {
    private static final Logger log = LoggerFactory.getLogger(DataSender.class);

    private final ReactiveSender reactiveSender;
    private final Consumer<StringValue> sendAsk;
    private final Flux<StringValue> valueFlux;
    private final String topicName;

    public DataSender(
            String topicName,
            ReactiveSender reactiveSender,
            Flux<StringValue> valueFlux,
            Consumer<StringValue> sendAsk) {
        this.topicName = topicName;
        this.sendAsk = sendAsk;
        this.valueFlux = valueFlux;
        this.reactiveSender = reactiveSender;
    }

    public void send() {
        reactiveSender
                .getSender()
                .send(valueFlux.map(value ->
                        SenderRecord.create(topicName, null, null, value.id(), value, new CorrelationMetadata(value))))
                .doOnError(error -> log.error("Send failed", error))
                .doOnNext(senderResult -> {
                    log.info(
                            "message id:{} was sent, offset:{}",
                            senderResult.correlationMetadata().value().id(),
                            senderResult.recordMetadata().offset());
                    sendAsk.accept(senderResult.correlationMetadata().value());
                })
                .subscribe();
    }
}
