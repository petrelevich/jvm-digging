package ru.petrelevich;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import org.reactivestreams.Subscriber;
import reactor.test.StepVerifier;

import static io.micronaut.http.HttpRequest.GET;
import static io.micronaut.http.HttpRequest.POST;
import static org.assertj.core.api.Assertions.assertThat;

class UserValueControllerTest extends DatabaseTestBase {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testHelloWorldResponseBlocking() {
        var response = client.toBlocking()
                .retrieve(GET("/hello"));

        assertThat(response).isEqualTo("Hello World");
    }

    @Test
    void testHelloWorldResponse() {
        var response = client
                .retrieve(GET("/hello"));

        StepVerifier.create(response)
                .expectNext("Hello World")
                .expectComplete()
                .verify();
    }

    @Test
    void testValue() {
        var value = "StrVal1";
        var creationResponse = client
                .retrieve(POST("/value", String.format("{\"val\":\"%s\"}", value))
                        .header("Content-Type", "application/json"));

        StepVerifier.create(creationResponse)
                .consumeNextWith(insertedId -> {
                        StepVerifier.create(client.retrieve(GET(String.format("/value/%d", Long.valueOf(insertedId)))))
                                .expectNext(value)
                                .expectComplete()
                                .verify();
                }).expectComplete()
                .verify();
    }
}