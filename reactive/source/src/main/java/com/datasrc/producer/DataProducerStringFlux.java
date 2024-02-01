package com.datasrc.producer;

import com.datasrc.logger.MdcLogger;
import java.time.Duration;
import java.util.function.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Scheduler;

@Service("dataProducerFlux")
public class DataProducerStringFlux implements DataProducer<Flux<StringValue>> {
    private static final Logger log = LoggerFactory.getLogger(DataProducerStringFlux.class);
    private final Scheduler timer;
    private final MdcLogger mdcLogger;

    public DataProducerStringFlux(Scheduler timer, MdcLogger logger) {
        this.timer = timer;
        this.mdcLogger = logger;
    }

    @Override
    public Flux<StringValue> produce(long seed) {
        log.info("produce using seed:{}", seed);
        var stringSeed = "someDataStr:";
        var dataSeq = Flux.generate(() -> seed, (BiFunction<Long, SynchronousSink<Long>, Long>) (prev, sink) -> {
                    var newValue = prev + 1;
                    sink.next(newValue);
                    return newValue;
                })
                .delayElements(Duration.ofSeconds(3), timer)
                .map(val -> new StringValue(stringSeed + val))
                .doOnNext(val -> log.info("without mdc:{}", val))
                .doOnEach(mdcLogger.log(val -> log.info("with mdc:{}", val)));

        log.info("produce method finished");
        return dataSeq;
    }
}
