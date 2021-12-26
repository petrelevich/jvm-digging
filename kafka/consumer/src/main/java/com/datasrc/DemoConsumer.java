package com.datasrc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoConsumer {
    private static final Logger log = LoggerFactory.getLogger(DemoConsumer.class);

    public static void main(String[] args)  {
        var consumer = new MyConsumer("localhost:9092");
        var dataConsumer = new StringValueConsumer(consumer, value -> log.info("value:{}", value));
        dataConsumer.startSending();
    }
}
