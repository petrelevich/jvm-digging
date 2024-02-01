package com.datasrc.logger;

import java.util.function.Consumer;
import reactor.core.publisher.Signal;

public interface MdcLogger {

    <T> Consumer<Signal<T>> log(Consumer<T> logStatement);
}
