package com.datasrc;

import com.datasrc.config.ReactiveReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.Schedulers;

public class DemoReactiveReceiver {
    private static final Logger log = LoggerFactory.getLogger(DemoReactiveReceiver.class);

    public static void main(String[] args) {
        String topicName = "MyTopic";
        var schedulerValueReceiver = Schedulers.newParallel("value-receiver", 1);

        var reactiveReceiver =
                new ReactiveReceiver("localhost:9092", topicName, schedulerValueReceiver);
        var dataConsumer =
                new StringValueConsumer(reactiveReceiver, value -> log.info("value:{}", value));
        dataConsumer.startConsume();
    }
}
