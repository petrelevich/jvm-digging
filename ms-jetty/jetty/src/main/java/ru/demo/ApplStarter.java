package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.demo.webserver.ServerJetty;

@Component
public class ApplStarter implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ApplStarter.class);
    private final ServerJetty serverJetty;

    public ApplStarter(ServerJetty serverJetty) {
        this.serverJetty = serverJetty;
    }

    @Override
    public void run(String... args) throws Exception {
        serverJetty.start();
        log.info("Application started");
    }
}
