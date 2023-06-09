package main.examples.services;

import jakarta.annotation.PostConstruct;
import main.examples.model.TableWithPk;
import main.examples.repository.SomeObjectRepository;
import main.examples.repository.TableWithPkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SomeService {
    private static final Logger log = LoggerFactory.getLogger(SomeService.class);
    private final SomeObjectRepository someObjectRepository;
    private final TableWithPkRepository tableWithPkRepository;

    public SomeService(
            SomeObjectRepository someObjectRepository,
            TableWithPkRepository tableWithPkRepository) {
        this.someObjectRepository = someObjectRepository;
        this.tableWithPkRepository = tableWithPkRepository;
    }

    @PostConstruct
    public void someAction() {
        var someObjectList = someObjectRepository.findAll();
        log.info("someAction");
        someObjectList.forEach(someObject -> log.info("someObject:{}", someObject));

        compositePk();
    }

    public void compositePk() {
        log.info(">>> TableWithPkRepository");
        var pk = new TableWithPk.Pk("p1", String.valueOf(System.currentTimeMillis()));
        TableWithPk tableWithPk = new TableWithPk(pk, "value", true);
        log.info("tableWithPk for save:{}", tableWithPk);

        tableWithPkRepository.saveEntry(tableWithPk);

        TableWithPk loadedTableWithPk = tableWithPkRepository.findById(pk).orElseThrow();
        log.info("loaded TableWithPk:{}", loadedTableWithPk);
    }
}
