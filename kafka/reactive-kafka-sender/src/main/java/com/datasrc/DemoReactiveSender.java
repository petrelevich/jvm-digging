package com.datasrc;

import com.datasrc.config.ReactiveSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.Schedulers;

public class DemoReactiveSender {
    private static final Logger log = LoggerFactory.getLogger(DemoReactiveSender.class);

    public static void main(String[] args) {
        var topicName = "MyTopic";

        var schedulerValueSource = Schedulers.newParallel("value-source", 1);
        var valueSource = new StringValueSource(schedulerValueSource);
        var valueFlow = valueSource.makeValueFlow();

        var schedulerKafka = Schedulers.newParallel("kafka", 1);
        var reactiveSender = new ReactiveSender("localhost:9092", schedulerKafka);

        var dataSender = new DataSender(
                topicName, reactiveSender, valueFlow, stringValue -> log.info("asked, value:{}", stringValue));
        dataSender.send();
    }
}
