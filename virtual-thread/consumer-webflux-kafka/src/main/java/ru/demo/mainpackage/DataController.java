package ru.demo.mainpackage;

import io.micrometer.context.integration.Slf4jThreadLocalAccessor;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import ru.demo.mainpackage.kafka.KafkaProducer;
import ru.demo.mainpackage.model.Request;
import ru.demo.mainpackage.model.RequestId;
import ru.demo.mainpackage.model.ResponseSum;

@RestController
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);
    private final AtomicLong idGenerator = new AtomicLong(0);

    private final KafkaProducer kafkaProducer;
    private final ResponseStorage stringValueStorage;
    private final Set<String> producersForWait = Set.of("producer-1", "producer-2", "producer-3");

    public DataController(KafkaProducer kafkaProducer, ResponseStorage stringValueStorage) {
        this.kafkaProducer = kafkaProducer;
        this.stringValueStorage = stringValueStorage;
    }

    /*
    curl -H "Content-Type: application/json" -X POST -d "344" http://localhost:8080/request
     */

    @PostMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseSum> responseSum(@RequestBody long requestData) {
        log.info("new requestData:{}", requestData);
        var request = new Request(new RequestId(idGenerator.incrementAndGet()), requestData);
        MDC.put("requestId", String.valueOf(request.id().value()));
        log.info("request:{}", request);

        kafkaProducer.send(request);
        return stringValueStorage
                .get(request.id(), producersForWait)
                .onErrorResume(error -> {
                    log.info("kafka timeout");
                    return Mono.just(new ResponseSum(request.id(), null));
                })
                .doOnNext(response -> log.info("response:{}", response))
                .contextWrite(Context.of(Slf4jThreadLocalAccessor.KEY, MDC.getCopyOfContextMap()));
    }
}
