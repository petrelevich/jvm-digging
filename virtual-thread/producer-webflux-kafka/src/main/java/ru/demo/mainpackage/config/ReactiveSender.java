package ru.demo.mainpackage.config;

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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Properties;
import java.util.function.Consumer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;
import ru.demo.mainpackage.model.Request;

public class ReactiveSender {
    private static final Logger log = LoggerFactory.getLogger(ReactiveSender.class);
    private final KafkaSender<Long, Request> sender;
    private final String topicName;

    public ReactiveSender(String bootstrapServers, Scheduler schedulerKafka, String topicName) {
        this.topicName = topicName;
        var props = new Properties();
        props.put(CLIENT_ID_CONFIG, "KafkaReactiveSender");
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ACKS_CONFIG, "all");
        props.put(RETRIES_CONFIG, 1);
        props.put(BATCH_SIZE_CONFIG, 16384);
        props.put(LINGER_MS_CONFIG, 10);
        props.put(BUFFER_MEMORY_CONFIG, 26384); // bytes
        props.put(MAX_BLOCK_MS_CONFIG, 1_000); // ms
        props.put(KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        var objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        props.put(OBJECT_MAPPER, objectMapper);

        SenderOptions<Long, Request> senderOptions =
                SenderOptions.<Long, Request>create(props).maxInFlight(10).scheduler(schedulerKafka);

        sender = KafkaSender.create(senderOptions);
    }

    public Mono<SenderResult<Request>> send(Request data, Consumer<Request> sendAsk) {
        return sender.send(Mono.just(
                        SenderRecord.create(topicName, null, null, data.id().value(), data, data)))
                .doOnError(error -> log.error("Send failed", error))
                .doOnNext(senderResult -> {
                    log.atDebug()
                            .setMessage("message id:{} was sent, offset:{}")
                            .addArgument(senderResult.correlationMetadata().id())
                            .addArgument(senderResult.recordMetadata().offset())
                            .log();
                    sendAsk.accept(senderResult.correlationMetadata());
                })
                .next();
    }

    public void close() {
        sender.close();
    }
}
