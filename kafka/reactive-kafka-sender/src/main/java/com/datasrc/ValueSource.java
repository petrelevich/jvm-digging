package com.datasrc;

import com.datasrc.model.StringValue;
import reactor.core.publisher.Flux;

public interface ValueSource {
    Flux<StringValue> makeValueFlow();
}
