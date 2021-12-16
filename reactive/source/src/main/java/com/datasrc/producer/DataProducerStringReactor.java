package com.datasrc.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import java.time.Duration;
import java.util.function.BiFunction;

@Service
public class DataProducerStringReactor implements DataProducer<Flux<StringValue>>{
    private static final Logger log = LoggerFactory.getLogger(DataProducerStringReactor.class);

    @Override
    public Flux<StringValue> produce(long seed) {
        log.info("produce using seed:{}", seed);
        var stringSeed = "someDataStr:";
        var dataSeq = Flux.generate(() -> seed,
                        (BiFunction<Long, SynchronousSink<Long>, Long>) (prev, sink) -> {
                            var newValue = prev + 1;
                            sink.next(newValue);
                            return newValue;
                        })
                .delayElements(Duration.ofSeconds(3))
                .map(val -> new StringValue(stringSeed + val))
                .doOnNext(val -> log.info("val:{}", val));


        log.info("produce method finished");
        return dataSeq;
    }
}
