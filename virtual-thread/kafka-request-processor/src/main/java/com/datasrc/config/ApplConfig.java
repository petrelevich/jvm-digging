package com.datasrc.config;

import com.datasrc.RequestProcessor;
import com.datasrc.kafka.KafkaConsumer;
import com.datasrc.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplConfig {
    private static final Logger log = LoggerFactory.getLogger(ApplConfig.class);

    @Bean
    public ProducerName producerName() {
        var nameFormEnv = System.getenv("PRODUCER_NAME");
        String producerName = nameFormEnv == null ? "defaultProducer" : nameFormEnv;
        log.info("producerName:{}", producerName);
        return new ProducerName(producerName);
    }

    @Bean(destroyMethod = "close")
    public KafkaConsumer kafkaConsumer(
            @Value("${application.kafka-bootstrap-servers}") String bootstrapServers,
            @Value("${application.topic-request}") String topicRequest,
            ProducerName producerName) {
        return new KafkaConsumer(bootstrapServers, topicRequest, producerName.value);
    }

    @Bean(destroyMethod = "close")
    public KafkaProducer kafkaProducer(
            @Value("${application.kafka-bootstrap-servers}") String bootstrapServers,
            @Value("${application.topic-response}") String topicResponse,
            ProducerName producerName) {
        return new KafkaProducer(bootstrapServers, topicResponse, producerName.value);
    }

    @Bean
    public RequestProcessor requestProcessor(
            KafkaConsumer kafkaConsumer, KafkaProducer kafkaProducer, ProducerName producerName) {
        return new RequestProcessor(kafkaConsumer, kafkaProducer, producerName.value);
    }

    public record ProducerName(String value) {}
}
