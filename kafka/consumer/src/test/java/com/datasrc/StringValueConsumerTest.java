package com.datasrc;

import static com.datasrc.JsonSerializer.OBJECT_MAPPER;
import static com.datasrc.MyConsumer.TOPIC_NAME;
import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.LongStream;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class StringValueConsumerTest {
    private static final Logger log = LoggerFactory.getLogger(StringValueConsumerTest.class);

    @BeforeAll
    public static void init() throws ExecutionException, InterruptedException, TimeoutException {
        KafkaBase.start(List.of(new NewTopic(TOPIC_NAME, 1, (short) 1)));
    }

    @Test
    void dataHandlerTest() {
        // given
        List<StringValue> stringValues =
                LongStream.range(0, 9)
                        .boxed()
                        .map(idx -> new StringValue(idx, "test:" + idx))
                        .toList();
        putValuesToKafka(stringValues);
        var myConsumer = new MyConsumer(KafkaBase.getBootstrapServers());

        List<StringValue> factStringValues = new CopyOnWriteArrayList<>();
        var dataConsumer = new StringValueConsumer(myConsumer, factStringValues::add);

        // when
        CompletableFuture.runAsync(dataConsumer::startSending);

        // then
        await().atMost(30, TimeUnit.SECONDS)
                .until(() -> factStringValues.size() == stringValues.size());

        assertThat(factStringValues).hasSameElementsAs(stringValues);
        dataConsumer.stopSending();
    }

    private void putValuesToKafka(List<StringValue> stringValues) {
        Properties props = new Properties();
        props.put(CLIENT_ID_CONFIG, "myKafkaTestProducer");
        props.put(BOOTSTRAP_SERVERS_CONFIG, KafkaBase.getBootstrapServers());
        props.put(ACKS_CONFIG, "0");
        props.put(LINGER_MS_CONFIG, 1);
        props.put(KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(OBJECT_MAPPER, new ObjectMapper());

        log.info("sending values, counter:{}", stringValues.size());
        try (var kafkaProducer = new KafkaProducer<Long, StringValue>(props)) {
            for (var value : stringValues) {
                kafkaProducer.send(new ProducerRecord<>(TOPIC_NAME, value.id(), value));
            }
        }
        log.info("done");
    }
}
