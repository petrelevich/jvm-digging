package main.examples.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;
import main.examples.model.Record;
import main.examples.model.RecordPackage;
import main.examples.repository.base.BasePersistenceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RecordPackageRepositoryTest extends BasePersistenceTest {

    @Autowired RecordPackageRepository repository;

    @Test
    void saveAndFindTest() {
        var record1 = new Record("r1");
        var record2 = new Record("r2");
        var record3 = new Record("r3");

        var recordPackage = new RecordPackage("package", Set.of(record1, record2, record3));

        var recordPackageSaved = repository.save(recordPackage);

        var recordPackageLoaded = repository.findById(recordPackageSaved.getRecordPackageId());

        assertThat(recordPackageLoaded).isPresent();
        assertThat(recordPackageLoaded.get().getName()).isEqualTo(recordPackage.getName());

        var recordList = recordPackageLoaded.get().getRecords();
        assertThat(recordList).isNotNull().hasSameSizeAs(recordPackage.getRecords());

        var dataList = recordList.stream().map(Record::getData).collect(Collectors.toList());
        assertThat(dataList).contains(record1.getData(), record2.getData(), record3.getData());
    }
}
