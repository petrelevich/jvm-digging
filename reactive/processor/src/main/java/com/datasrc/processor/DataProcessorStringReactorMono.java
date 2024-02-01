package com.datasrc.processor;

import com.datasrc.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("dataProcessorMono")
public class DataProcessorStringReactorMono implements DataProcessor<Mono<StringValue>> {
    private static final Logger log = LoggerFactory.getLogger(DataProcessorStringReactorMono.class);

    @Override
    public Mono<StringValue> process(Mono<StringValue> dataflow) {
        log.info("processor");
        var dataSeq = dataflow.map(val -> new StringValue(val.value().toUpperCase()))
                .doOnNext(val -> log.info("out val:{}", val));

        log.info("processor method finished");
        return dataSeq;
    }
}
