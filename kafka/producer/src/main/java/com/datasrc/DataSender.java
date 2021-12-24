package com.datasrc;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;


import static com.datasrc.MyProducer.TOPIC_NAME;


public class DataSender {
    private static final Logger log = LoggerFactory.getLogger(DataSender.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final MyProducer myProducer;
    private final Consumer<StringValue> sendAsk;

    public DataSender(MyProducer myProducer, Consumer<StringValue> sendAsk) {
        this.sendAsk = sendAsk;
        this.myProducer = myProducer;
    }

    public void dataHandler(StringValue value) {
        log.info("value:{}", value);
        try {
            var valueAsString = mapper.writeValueAsString(value);
            myProducer.getMyProducer().send(new ProducerRecord<>(TOPIC_NAME, String.valueOf(value.id()), valueAsString),
                    (metadata, exception) -> {
                        if (exception != null) {
                            log.error("message wasn't sent", exception);
                        } else {
                            log.info("message id:{} was sent, offset:{}", value.id(), metadata.offset());
                            sendAsk.accept(value);
                        }
                    });
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
