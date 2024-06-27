package ru.demo.mainpackage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.demo.mainpackage.model.KafkaTopicName;

@Configuration
public class ApplConfig {

    @Bean(destroyMethod = "close")
    public Producer requestSender(@Value("${application.kafka-bootstrap-servers}") String bootstrapServers) {
        return new Producer(bootstrapServers);
    }

    @Bean
    public KafkaTopicName kafkaTopicName(@Value("${application.topic-request}") String topic) {
        return new KafkaTopicName(topic);
    }
}
