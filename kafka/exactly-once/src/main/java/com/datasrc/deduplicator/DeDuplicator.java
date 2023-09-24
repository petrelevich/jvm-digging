package com.datasrc.deduplicator;

import com.datasrc.model.StringValue;

public interface DeDuplicator {
    boolean process(StringValue value, StringValueProcessor processor);
}
