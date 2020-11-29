package ru.petrelevich.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.petrelevich.model.Record;
import ru.petrelevich.model.RecordPackage;
import ru.petrelevich.repository.base.BasePersistenceTest;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class RecordPackageRepositoryTest extends BasePersistenceTest {

    @Autowired
    RecordPackageRepository repository;

    @Test
    void saveAndFindTest() {
        var record1 = new Record("r1");
        var record2 = new Record("r2");
        var record3 = new Record( "r3");

        var recordPackage = new RecordPackage( "package", Set.of(record1, record2, record3));

        var recordPackageSaved = repository.save(recordPackage);

        var recordPackageLoaded = repository.findById(recordPackageSaved.getRecordPackageId());

        assertThat(recordPackageLoaded).isPresent();
        assertThat(recordPackageLoaded.get().getName()).isEqualTo(recordPackage.getName());

        var recordList = recordPackageLoaded.get().getRecords();
        assertThat(recordList).isNotNull();
        assertThat(recordList.size()).isEqualTo(recordPackage.getRecords().size());

        var dataList = recordList.stream().map(Record::getData).collect(Collectors.toList());
        assertThat(dataList).contains(record1.getData(), record2.getData(), record3.getData());
    }
}
