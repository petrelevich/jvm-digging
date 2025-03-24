package ru.demo.bucket4j;

import io.github.bucket4j.BucketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BucketListenerLogger implements BucketListener {
    private static final Logger log = LoggerFactory.getLogger(BucketListenerLogger.class);

    @Override
    public void onConsumed(long tokens) {
        log.info("onConsumed, tokens:{}", tokens);
    }

    @Override
    public void onRejected(long tokens) {
        log.info("onRejected, tokens:{}", tokens);
    }

    @Override
    public void onParked(long nanos) {
        log.info("onParked, nanos:{}", nanos);
    }

    @Override
    public void onInterrupted(InterruptedException e) {
        log.info("onInterrupted");
    }

    @Override
    public void onDelayed(long nanos) {
        log.info("onDelayed, nanos:{}", nanos);
    }
}
