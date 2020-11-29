package ru.petrelevich.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.petrelevich.model.InfoAdditional;
import ru.petrelevich.model.InfoMain;
import ru.petrelevich.model.Record;
import ru.petrelevich.model.RecordPackage;
import ru.petrelevich.repository.InfoMainRepository;
import ru.petrelevich.repository.RecordPackageRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class OneToOneService {
    private static final Logger logger = LoggerFactory.getLogger(OneToOneService.class);
    private final InfoMainRepository infoMainRepository;

    public OneToOneService(InfoMainRepository infoMainRepository) {
        this.infoMainRepository = infoMainRepository;
    }

    //@PostConstruct
    public void someAction() {
        logger.info("creating into");
        var infoAdditional = new InfoAdditional("InfoAdditional");

        var infoMain = new InfoMain("mainData", infoAdditional);

        var infoMainSaved = infoMainRepository.save(infoMain);
        var infoMainLoaded = infoMainRepository.findById(infoMainSaved.getInfoMainId());

        infoMainLoaded.ifPresent(loaded -> System.out.println(loaded.toString()));

        logger.info("creating info done");
    }
}
