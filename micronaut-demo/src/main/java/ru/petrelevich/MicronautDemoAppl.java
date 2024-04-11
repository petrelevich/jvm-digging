package ru.petrelevich;

import io.micronaut.runtime.Micronaut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MicronautDemoAppl {
    private static final Logger log = LoggerFactory.getLogger(MicronautDemoAppl.class);

    public static void main(String[] args) {
        Micronaut.run(MicronautDemoAppl.class, args);
        log.info("started");
    }
}