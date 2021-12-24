package com.datasrc;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


public class StringValueConsumer {
    private static final Logger log = LoggerFactory.getLogger(StringValueConsumer.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final MyConsumer myConsumer;
    private final Duration timeout = Duration.ofMillis(1000);
    private final AtomicBoolean stopFlag = new AtomicBoolean(false);
    private final Consumer<StringValue> dataConsumer;

    public StringValueConsumer(MyConsumer myConsumer, Consumer<StringValue> dataConsumer) {
        this.myConsumer = myConsumer;
        this.dataConsumer = dataConsumer;
    }

    public void dataHandler() {
        while (!stopFlag.get()) {
            ConsumerRecords<String, String> records = myConsumer.getConsumer().poll(timeout);
            for (ConsumerRecord<String, String> kafkaRecord : records) {
                try {
                    var key = mapper.readValue(kafkaRecord.key(), Long.class);
                    var value = mapper.readValue(kafkaRecord.value(), StringValue.class);
                    log.info("key:{}, value:{}, record:{}", key, value, kafkaRecord);
                    dataConsumer.accept(value);
                } catch (JacksonException ex) {
                    log.error("can't parse record:{}", kafkaRecord, ex);
                }
            }
        }
    }

    public void setStopFlag(boolean flag) {
        stopFlag.set(flag);
    }
}
