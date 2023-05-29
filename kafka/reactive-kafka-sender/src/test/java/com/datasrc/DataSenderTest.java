package com.datasrc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.datasrc.base.BaseTest;
import com.datasrc.base.KafkaBase;
import com.datasrc.config.ReactiveSender;
import com.datasrc.model.StringValue;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.LongStream;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.scheduler.Schedulers;

class DataSenderTest extends BaseTest {
    private static final String TOPIC_NAME = "MyTopicTest";

    @BeforeAll
    public static void init() throws ExecutionException, InterruptedException, TimeoutException {
        KafkaBase.start(List.of(new NewTopic(TOPIC_NAME, 1, (short) 1)));
    }

    @Test
    void dataHandlerTest() {
        // given
        List<StringValue> stringValues =
                LongStream.range(1, 9)
                        .boxed()
                        .map(idx -> new StringValue(idx, "stVal:" + idx))
                        .toList();

        var schedulerKafka = Schedulers.newParallel("kafka-test", 1);
        var reactiveSender = new ReactiveSender(KafkaBase.getBootstrapServers(), schedulerKafka);

        var schedulerValueSource = Schedulers.newParallel("value-source-test", 1);
        var valueSource = new StringValueSource(schedulerValueSource);
        var valueFlow = valueSource.makeValueFlow();

        List<StringValue> factStringValues = new CopyOnWriteArrayList<>();

        var dataSender =
                new DataSender(TOPIC_NAME, reactiveSender, valueFlow, factStringValues::add);

        // when
        dataSender.send();

        // then
        await().atMost(20, TimeUnit.SECONDS)
                .until(() -> factStringValues.size() == stringValues.size());
        assertThat(factStringValues).hasSameElementsAs(stringValues);
    }
}
