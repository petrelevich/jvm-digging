package ru.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RestClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(RestClient.class).run(args);
    }
}
