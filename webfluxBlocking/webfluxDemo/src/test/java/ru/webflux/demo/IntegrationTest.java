package ru.webflux.demo;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTest extends BaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void checkBlockhoundWorks() {
        var mono = Mono.delay(Duration.ofMillis(1))
                .doOnNext(it -> {
                    try {
                        System.out.println(Thread.currentThread().getName());
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        StepVerifier.create(mono)
                .expectErrorMatches(error -> error instanceof BlockingOperationError)
                .verify();
    }

    @Test
    void dataTest() {
        //given
        var value = 12;
        var webTestClientForTest = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(20))
                .build();

        //when
        var result = webTestClientForTest
                .get().uri(String.format("/data/%d", value))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

        //then
        StepVerifier.create(result)
                .expectNext(String.format("%d", value))
                .verifyComplete();
    }

    @Test
    void dataTestReact() {
        //given
        var value = 12;
        var webTestClientForTest = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(20))
                .build();

        //when
        var result = webTestClientForTest
                .get().uri(String.format("/dataReact/%d", value))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

        //then
        StepVerifier.create(result)
                .expectNext(String.format("%d", value))
                .verifyComplete();
    }
}
