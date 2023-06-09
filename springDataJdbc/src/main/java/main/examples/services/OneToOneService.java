package main.examples.services;

import main.examples.model.InfoAdditional;
import main.examples.model.InfoMain;
import main.examples.repository.InfoMainRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OneToOneService {
    private static final Logger logger = LoggerFactory.getLogger(OneToOneService.class);
    private final InfoMainRepository infoMainRepository;

    public OneToOneService(InfoMainRepository infoMainRepository) {
        this.infoMainRepository = infoMainRepository;
    }

    // @PostConstruct
    public void someAction() {
        logger.info("creating into");
        var infoAdditional = new InfoAdditional("InfoAdditional");

        var infoMain = new InfoMain("mainData", infoAdditional);

        var infoMainSaved = infoMainRepository.save(infoMain);
        var infoMainLoaded = infoMainRepository.findById(infoMainSaved.getInfoMainId());

        infoMainLoaded.ifPresent(loaded -> logger.info("infoMainLoaded:{}", loaded));

        logger.info("creating info done");
    }
}
