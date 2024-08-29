package com.datasrc;

import com.datasrc.kafka.KafkaConsumer;
import com.datasrc.kafka.KafkaProducer;
import com.datasrc.model.Response;
import com.datasrc.model.ResponseId;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

public class RequestProcessor implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(RequestProcessor.class);
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final KafkaConsumer consumer;
    private final KafkaProducer kafkaProducer;
    private final String producerName;

    public RequestProcessor(KafkaConsumer kafkaConsumer, KafkaProducer producer, String producerName) {
        this.consumer = kafkaConsumer;
        this.kafkaProducer = producer;
        this.producerName = producerName;
    }

    @Override
    public void run(String... args) {
        while (!Thread.currentThread().isInterrupted()) {
            var requests = consumer.poll();
            for (var request : requests) {
                log.info("income request:{}", request);
                var response = new Response(
                        request.id(), producerName, new ResponseId(idGenerator.incrementAndGet()), request.data() * 10);
                kafkaProducer.send(response);
            }
        }
    }
}
