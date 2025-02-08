package ru.demo.resilience4j;

import static io.github.resilience4j.circuitbreaker.CircuitBreaker.State.HALF_OPEN;
import static io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN;
import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CircuitBreakerDemo {
    private static final Logger log = LoggerFactory.getLogger(CircuitBreakerDemo.class);

    private int execCounter = 0;
    private int attemptNo = 0;
    private final AtomicReference<TestState> currentState = new AtomicReference<>(TestState.WAIT_OPEN);

    public static void main(String[] args) {
        new CircuitBreakerDemo().go();
    }

    private void go() {
        var circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(6))
                .permittedNumberOfCallsInHalfOpenState(3)
                .slidingWindowSize(10)
                .slidingWindowType(COUNT_BASED)
                .build();

        var circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);

        var circuitBreaker = circuitBreakerRegistry.circuitBreaker("testCB");
        circuitBreaker
                .getEventPublisher()
                .onFailureRateExceeded(
                        event -> log.info("EventType:{}, FailureRate:{}", event.getEventType(), event.getFailureRate()))
                .onError(event -> log.info(
                        "EventType:{}, errorMsg:{}",
                        event.getEventType(),
                        event.getThrowable().getMessage()))
                .onCallNotPermitted(event -> log.info("EventType:{}", event.getEventType()))
                .onStateTransition(event -> {
                    log.info(
                            "EventType:{}, {} -> {}",
                            event.getEventType(),
                            event.getStateTransition().getFromState(),
                            event.getStateTransition().getToState());
                    if (event.getStateTransition().getToState().equals(OPEN)) {
                        currentState.set(TestState.WAIT_HALF_OPEN);
                    }
                    if (event.getStateTransition().getToState().equals(HALF_OPEN)) {
                        currentState.set(TestState.WAIT_CLOSED);
                    }
                });

        var decoratedSupplierOk = CircuitBreaker.decorateSupplier(circuitBreaker, this::action);
        var decoratedSupplierException = CircuitBreaker.decorateSupplier(circuitBreaker, this::actionException);

        // IntStream.range(0, 10).forEach(val -> decoratedSupplierOk.get());

        runException(decoratedSupplierOk, decoratedSupplierException);
    }

    private void runException(Supplier<String> decoratedSupplierOk, Supplier<String> decoratedSupplierException) {
        IntStream.range(0, 100).forEach(val -> {
            try {
                log.info("attemptNo:{}", attemptNo++);
                sleep(3);

                if (currentState.get().equals(TestState.WAIT_OPEN)) {
                    decoratedSupplierException.get();
                } else {
                    decoratedSupplierOk.get();
                    if (attemptNo % 10 == 0) {
                        decoratedSupplierException.get();
                    }
                }
            } catch (Exception ex) {
                log.error("error:{}", ex.getMessage());
            }
        });
    }

    private String action() {
        var currentCounter = execCounter++;
        log.info("some action, counter:{}", currentCounter);
        sleep(1);
        return String.format("resultDone:%d", currentCounter);
    }

    private String actionException() {
        var currentCounter = execCounter++;
        log.info("exception action, counter:{}", currentCounter);
        sleep(1);
        throw new RuntimeException("TestError");
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(Duration.ofSeconds(seconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private enum TestState {
        WAIT_OPEN,
        WAIT_HALF_OPEN,
        WAIT_CLOSED
    }
}
