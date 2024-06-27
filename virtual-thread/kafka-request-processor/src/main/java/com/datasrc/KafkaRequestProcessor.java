package com.datasrc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaRequestProcessor {

    public static void main(String[] args) {
        SpringApplication.run(KafkaRequestProcessor.class, args);
    }
}
