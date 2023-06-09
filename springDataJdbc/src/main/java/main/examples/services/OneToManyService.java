package main.examples.services;

import java.util.Set;
import main.examples.model.Record;
import main.examples.model.RecordPackage;
import main.examples.repository.RecordPackageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OneToManyService {
    private static final Logger logger = LoggerFactory.getLogger(OneToManyService.class);
    private final RecordPackageRepository recordPackageRepository;

    public OneToManyService(RecordPackageRepository recordPackageRepository) {
        this.recordPackageRepository = recordPackageRepository;
    }

    //  @PostConstruct
    public void someAction() {
        logger.info("creating package");
        var record1 = new Record("r1");
        var record2 = new Record("r2");
        var record3 = new Record("r3");

        var recordPackage = new RecordPackage("package", Set.of(record1, record2, record3));

        var recordPackageSaved = recordPackageRepository.save(recordPackage);

        var recordPackageLoaded =
                recordPackageRepository.findById(recordPackageSaved.getRecordPackageId());

        recordPackageLoaded.ifPresent(loaded -> logger.info("loadedRecord:{}", loaded));

        logger.info("creating package done");
    }
}
