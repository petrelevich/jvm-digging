package main.examples.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import main.examples.model.Record;
import main.examples.model.RecordPackage;
import main.examples.repository.RecordPackageRepository;


import java.util.Set;

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
        var record3 = new Record( "r3");

        var recordPackage = new RecordPackage( "package", Set.of(record1, record2, record3));

        var recordPackageSaved = recordPackageRepository.save(recordPackage);

        var recordPackageLoaded = recordPackageRepository.findById(recordPackageSaved.getRecordPackageId());

        recordPackageLoaded.ifPresent(loaded -> System.out.println(loaded.toString()));

        logger.info("creating package done");
    }
}
