package com.datasrc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class StringValueDeserializer implements Deserializer<StringValue> {
    public static final String OBJECT_MAPPER = "objectMapper";
    private final String encoding = StandardCharsets.UTF_8.name();
    private ObjectMapper mapper;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        mapper = (ObjectMapper) configs.get(OBJECT_MAPPER);
        if (mapper == null) {
            throw new IllegalArgumentException("config property OBJECT_MAPPER was not set");
        }
    }

    @Override
    public StringValue deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }
            else {
                var valueAsString = new String(data, encoding);
                return mapper.readValue(valueAsString, StringValue.class);
            }
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to StringValue", e);
        }
    }
}
