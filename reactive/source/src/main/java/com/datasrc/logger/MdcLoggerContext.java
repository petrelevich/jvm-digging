package com.datasrc.logger;

import java.util.List;
import java.util.function.Consumer;
import org.slf4j.MDC;
import reactor.core.publisher.Signal;

public class MdcLoggerContext implements MdcLogger {

    private final List<MdcField> mdcFieldList;

    public MdcLoggerContext(List<MdcField> mdcFieldList) {
        this.mdcFieldList = mdcFieldList;
    }

    @Override
    public <T> Consumer<Signal<T>> log(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnNext()) {
                return;
            }
            var contextView = signal.getContextView();
            try {
                for (var mdcField : mdcFieldList) {
                    if (contextView.hasKey(mdcField.name())) {
                        Object value = contextView.get(mdcField.name());
                        MDC.put(mdcField.name(), String.valueOf(value));
                    }
                }
                logStatement.accept(signal.get());
            } finally {
                MDC.clear();
            }
        };
    }
}
