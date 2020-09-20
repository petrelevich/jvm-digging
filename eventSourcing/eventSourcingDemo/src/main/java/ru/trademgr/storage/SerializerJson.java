package ru.trademgr.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.trademgr.events.CreatedTradeEvent;

public class SerializerJson implements Serializer {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String serializeEvent(CreatedTradeEvent event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public CreatedTradeEvent deserializeEvent(String eventAsString) {
        try {
            return mapper.readValue(eventAsString, CreatedTradeEvent.class);
        } catch (JsonProcessingException e) {
            throw new StorageException(e);
        }
    }
}
