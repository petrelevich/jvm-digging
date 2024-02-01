package com.datasrc.processor;

import com.datasrc.StringValue;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

@Service("dataProcessorFlux")
public class DataProcessorStringReactorFlux implements DataProcessor<Flux<StringValue>> {
    private static final Logger log = LoggerFactory.getLogger(DataProcessorStringReactorFlux.class);
    private final Scheduler timer;

    public DataProcessorStringReactorFlux(Scheduler timer) {
        this.timer = timer;
    }

    @Override
    public Flux<StringValue> process(Flux<StringValue> dataflow) {
        log.info("processor");
        var dataSeq = dataflow.delayElements(Duration.ofSeconds(5), timer)
                .map(val -> new StringValue(val.value().toUpperCase()))
                .doOnNext(val -> log.info("out val:{}", val));

        log.info("processor method finished");
        return dataSeq;
    }
}
