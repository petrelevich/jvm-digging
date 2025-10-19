package com.datasrc.kafka;

import static com.datasrc.json.JsonDeserializer.OBJECT_MAPPER;
import static com.datasrc.json.JsonDeserializer.TYPE_REFERENCE;
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

import com.datasrc.ConsumerException;
import com.datasrc.json.JsonDeserializer;
import com.datasrc.model.Request;
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

@SuppressWarnings({"java:S2245"})
public final class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private final Random random = new Random();
    private final org.apache.kafka.clients.consumer.KafkaConsumer<Long, Request> consumer;

    public static final int MAX_POLL_INTERVAL_MS = 300;
    private final Duration timeout = Duration.ofMillis(2_000);

    public KafkaConsumer(String bootstrapServers, String topic, String groupId) {
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(GROUP_ID_CONFIG, groupId);
        props.put(GROUP_INSTANCE_ID_CONFIG, makeGroupInstanceIdConfig(groupId));
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(OBJECT_MAPPER, new ObjectMapper());
        props.put(TYPE_REFERENCE, new TypeReference<Request>() {});

        props.put(MAX_POLL_RECORDS_CONFIG, 3);
        props.put(MAX_POLL_INTERVAL_MS_CONFIG, MAX_POLL_INTERVAL_MS);

        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public void close() {
        consumer.close();
    }

    public List<Request> poll() {
        ConsumerRecords<Long, Request> records = consumer.poll(timeout);
        List<Request> requests = new ArrayList<>(records.count());
        for (ConsumerRecord<Long, Request> kafkaRecord : records) {
            try {
                requests.add(kafkaRecord.value());
            } catch (Exception ex) {
                log.error("can't parse record:{}", kafkaRecord, ex);
            }
        }
        return requests;
    }

    public String makeGroupInstanceIdConfig(String groupId) {
        try {
            var hostName = InetAddress.getLocalHost().getHostName();
            return String.join("-", groupId, hostName, String.valueOf(random.nextInt(100_999_999)));
        } catch (Exception ex) {
            throw new ConsumerException("can't make GroupInstanceIdConfig", ex);
        }
    }
}
