package ru.demo.mainpackage.kafka;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.GROUP_INSTANCE_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static ru.demo.mainpackage.config.JsonSerializer.OBJECT_MAPPER;
import static ru.demo.mainpackage.json.JsonDeserializer.TYPE_REFERENCE;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.InetAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.mainpackage.json.JsonDeserializer;
import ru.demo.mainpackage.model.Response;

@SuppressWarnings("java:S2245")
public final class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private final Random random = new Random();
    private final org.apache.kafka.clients.consumer.KafkaConsumer<Long, Response> consumer;

    public static final String GROUP_ID_CONFIG_NAME = "requestProcessorConsumer";
    public static final int MAX_POLL_INTERVAL_MS = 300;
    private final Duration timeout = Duration.ofMillis(2_000);

    public KafkaConsumer(String bootstrapServers, String topic) {
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(GROUP_ID_CONFIG, GROUP_ID_CONFIG_NAME);
        props.put(GROUP_INSTANCE_ID_CONFIG, makeGroupInstanceIdConfig());
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(OBJECT_MAPPER, new ObjectMapper());
        props.put(TYPE_REFERENCE, new TypeReference<Response>() {});

        props.put(MAX_POLL_RECORDS_CONFIG, 3);
        props.put(MAX_POLL_INTERVAL_MS_CONFIG, MAX_POLL_INTERVAL_MS);

        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public void close() {
        consumer.close();
    }

    public List<Response> poll() {
        ConsumerRecords<Long, Response> records = consumer.poll(timeout);
        List<Response> responses = new ArrayList<>(records.count());
        for (ConsumerRecord<Long, Response> kafkaRecord : records) {
            try {
                responses.add(kafkaRecord.value());
            } catch (Exception ex) {
                log.error("can't parse record:{}", kafkaRecord, ex);
            }
        }
        return responses;
    }

    public String makeGroupInstanceIdConfig() {
        try {
            var hostName = InetAddress.getLocalHost().getHostName();
            return String.join("-", GROUP_ID_CONFIG_NAME, hostName, String.valueOf(random.nextInt(100_999_999)));
        } catch (Exception ex) {
            throw new ConsumerException("can't make GroupInstanceIdConfig", ex);
        }
    }
}
