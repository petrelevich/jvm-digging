package ru.demo.mainpackage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.demo.mainpackage.config.Producer;
import ru.demo.mainpackage.model.KafkaTopicName;
import ru.demo.mainpackage.model.Request;
import ru.demo.mainpackage.model.RequestId;

@RestController
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);
    private final AtomicLong idGenerator = new AtomicLong(0);

    private final Producer producer;
    private final KafkaTopicName kafkaTopicName;

    public DataController(Producer producer, KafkaTopicName kafkaTopicName) {
        this.producer = producer;
        this.kafkaTopicName = kafkaTopicName;
    }

    /*
    curl -H "Content-Type: application/json" -X POST -d "344" http://localhost:8080/request
     */

    @PostMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long request(@RequestBody long requestData)
            throws ExecutionException, InterruptedException, TimeoutException {
        log.atDebug()
                .setMessage("isVirtual:{}, requestData:{}")
                .addArgument(() -> Thread.currentThread().isVirtual())
                .addArgument(requestData)
                .log();
        var request = new Request(new RequestId(idGenerator.incrementAndGet()), requestData);

        var future = producer.getKafkaProducer()
                .send(
                        new ProducerRecord<>(kafkaTopicName.name(), request.id().value(), request),
                        (metadata, exception) -> {
                            if (exception != null) {
                                log.error("message wasn't sent", exception);
                            } else {
                                log.atDebug()
                                        .setMessage("isVirtual:{}, message id:{} was sent, offset:{}")
                                        .addArgument(
                                                () -> Thread.currentThread().isVirtual())
                                        .addArgument(request.id())
                                        .addArgument(metadata.offset())
                                        .log();
                            }
                        });
        var result = future.get(5, TimeUnit.SECONDS);
        log.atDebug()
                .setMessage("requestData:{}, offset:{}")
                .addArgument(requestData)
                .addArgument(result::offset)
                .log();

        return request.id().value();
    }
}
