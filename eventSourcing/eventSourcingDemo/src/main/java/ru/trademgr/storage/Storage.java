package ru.trademgr.storage;

import java.util.List;

public interface Storage {
    void saveEvent(String event);
    List<String> loadEvents();
}
