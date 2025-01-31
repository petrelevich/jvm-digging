package ru.demo.mainpackage.kafka;

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
import static ru.demo.mainpackage.config.JsonSerializer.OBJECT_MAPPER;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Properties;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.mainpackage.config.JsonSerializer;
import ru.demo.mainpackage.model.Request;

public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final org.apache.kafka.clients.producer.KafkaProducer<Long, Request> producer;
    private final String requestTopic;

    public KafkaProducer(String bootstrapServers, String requestTopic) {
        this.requestTopic = requestTopic;
        Properties props = new Properties();
        props.put(CLIENT_ID_CONFIG, "webfluxRequestProducer");
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ACKS_CONFIG, "all");
        props.put(RETRIES_CONFIG, 1);
        props.put(BATCH_SIZE_CONFIG, 16384);
        props.put(LINGER_MS_CONFIG, 10);
        props.put(BUFFER_MEMORY_CONFIG, 33_554_432); // bytes
        props.put(MAX_BLOCK_MS_CONFIG, 1_000); // ms
        props.put(KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(OBJECT_MAPPER, new ObjectMapper());

        producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
    }

    public void close() {
        producer.close();
    }

    public void send(Request request) {
        producer.send(new ProducerRecord<>(requestTopic, request.id().value(), request), (metadata, exception) -> {
            if (exception != null) {
                log.error("message wasn't sent", exception);
            }
        });
    }
}
