package ru.petrelevich.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.petrelevich.repository.RepositorySomeObject;

import javax.annotation.PostConstruct;

@Service
public class SomeService {
    private static final Logger logger = LoggerFactory.getLogger(SomeService.class);
    private final RepositorySomeObject repositorySomeObject;

    public SomeService(RepositorySomeObject repositorySomeObject) {
        this.repositorySomeObject = repositorySomeObject;
    }

    @PostConstruct
    public void someAction() {
        var someObjectList = repositorySomeObject.findAll();
        logger.info("someAction");
        someObjectList.forEach(someObject -> logger.info("someObject:{}", someObject));
    }
}
