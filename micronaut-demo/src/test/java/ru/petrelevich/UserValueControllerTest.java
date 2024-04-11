package ru.petrelevich;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class UserValueControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testHelloWorldResponseBlocking() {
        var response = client.toBlocking()
                .retrieve(HttpRequest.GET("/hello"));

        assertThat(response).isEqualTo("Hello World");
    }

    @Test
    void testHelloWorldResponse() {
        var response = client
                .retrieve(HttpRequest.GET("/hello"));

        StepVerifier.create(response)
                .expectNext("Hello World")
                .expectComplete()
                .verify();
    }
}