package ru.trademgr.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class StorageFile implements Storage {
    private static final Logger log = LoggerFactory.getLogger(StorageFile.class);
    private final Path path;

    public StorageFile(Path fileName) {
        log.info("Using filename:{} for data storage", fileName);
        this.path = fileName;
    }

    @Override
    public void saveEvent(String event) {
        try {
            Files.writeString(path, event + System.lineSeparator(), CREATE, APPEND);
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

    @Override
    public List<String> loadEvents() {
        try {
            if (!Files.exists(path)) {
                return Collections.emptyList();
            }
            try (Stream<String> stream = Files.lines(path)) {
                return stream.collect(Collectors.toList());
            }
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }
}
