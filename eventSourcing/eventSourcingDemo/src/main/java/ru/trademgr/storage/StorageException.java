package ru.trademgr.storage;

import com.fasterxml.jackson.core.JsonProcessingException;

public class StorageException extends RuntimeException {
    public StorageException(Exception e) {
        super(e);
    }
}
