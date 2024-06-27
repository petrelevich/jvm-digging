package ru.demo.mainpackage;

import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.demo.mainpackage.config.ReactiveSender;
import ru.demo.mainpackage.model.Request;
import ru.demo.mainpackage.model.RequestId;

@RestController
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);
    private final AtomicLong idGenerator = new AtomicLong(0);

    private final ReactiveSender requestSender;

    public DataController(ReactiveSender requestSender) {
        this.requestSender = requestSender;
    }

    /*
    curl -H "Content-Type: application/json" -X POST -d "344" http://localhost:8080/request
     */

    @PostMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Long> dataMono(@RequestBody long requestData) {
        log.atDebug().setMessage("requestData:{}").addArgument(requestData).log();

        var request = new Request(new RequestId(idGenerator.incrementAndGet()), requestData);

        return requestSender
                .send(request, requestSend -> log.atDebug()
                        .setMessage("send ok: {}")
                        .addArgument(requestSend)
                        .log())
                .map(requestSenderResult ->
                        requestSenderResult.correlationMetadata().id().value());
    }
}
