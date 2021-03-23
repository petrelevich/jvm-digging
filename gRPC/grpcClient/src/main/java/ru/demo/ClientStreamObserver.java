package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.numbers.NumberResponse;

import java.util.concurrent.CountDownLatch;


public class ClientStreamObserver implements io.grpc.stub.StreamObserver<NumberResponse> {
    private static final Logger log = LoggerFactory.getLogger(ClientStreamObserver.class);
    private final CountDownLatch latch;

    public ClientStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(NumberResponse numberResponse) {
        log.info("new value:{}", numberResponse.getNumber());
    }

    @Override
    public void onError(Throwable e) {
        log.error("got error", e);
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        log.info("request completed");
        latch.countDown();
    }
}
