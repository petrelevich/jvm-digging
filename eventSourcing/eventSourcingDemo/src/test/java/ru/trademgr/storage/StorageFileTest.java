package ru.trademgr.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class StorageFileTest {

    @Test
    void saveAndLoadTest(@TempDir Path queueDir) {
        //given
        Path queueFile = Paths.get(queueDir.toAbsolutePath().toString(), "queue.dat");
        var storage = new StorageFile(queueFile);
        var stringList = List.of("str1", "str2", "str3");

        //when
        for (var str: stringList) {
            storage.saveEvent(str);
        }
        var restoredList = storage.loadEvents();

        //then
        assertThat(restoredList).isEqualTo(stringList);
    }
}