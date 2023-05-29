package com.datasrc.config;

import static com.datasrc.config.JsonSerializer.OBJECT_MAPPER;
import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BUFFER_MEMORY_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.MAX_BLOCK_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import com.datasrc.model.StringValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Properties;
import org.apache.kafka.common.serialization.LongSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.Scheduler;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

public class ReactiveSender {
    private static final Logger log = LoggerFactory.getLogger(ReactiveSender.class);
    private final KafkaSender<Long, StringValue> sender;

    public ReactiveSender(String bootstrapServers, Scheduler schedulerKafka) {
        var props = new Properties();
        props.put(CLIENT_ID_CONFIG, "KafkaReactiveSender");
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ACKS_CONFIG, "1");
        props.put(RETRIES_CONFIG, 1);
        props.put(BATCH_SIZE_CONFIG, 16384);
        props.put(LINGER_MS_CONFIG, 10);
        props.put(BUFFER_MEMORY_CONFIG, 26384); // bytes
        props.put(MAX_BLOCK_MS_CONFIG, 1_000); // ms
        props.put(KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(OBJECT_MAPPER, new ObjectMapper());

        SenderOptions<Long, StringValue> senderOptions =
                SenderOptions.<Long, StringValue>create(props)
                        .maxInFlight(10)
                        .scheduler(schedulerKafka);

        sender = KafkaSender.create(senderOptions);

        var shutdownHook =
                new Thread(
                        () -> {
                            log.info("closing kafka sender");
                            sender.close();
                        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public KafkaSender<Long, StringValue> getSender() {
        return sender;
    }
}
