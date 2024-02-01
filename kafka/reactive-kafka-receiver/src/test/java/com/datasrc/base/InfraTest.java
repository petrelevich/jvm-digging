package com.datasrc.base;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class InfraTest extends BaseTest {
    @Test
    @SuppressWarnings("java:S2925")
    void checkBlockhoundWorks() {
        var mono = Mono.delay(Duration.ofMillis(1)).doOnNext(it -> {
            try {
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        StepVerifier.create(mono)
                .expectErrorMatches(error -> error instanceof BlockingOperationError)
                .verify();
    }
}
