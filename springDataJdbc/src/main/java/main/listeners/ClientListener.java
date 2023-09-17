package main.listeners;

import main.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.relational.core.mapping.event.AbstractRelationalEventListener;
import org.springframework.data.relational.core.mapping.event.AfterSaveEvent;

public class ClientListener extends AbstractRelationalEventListener<Client> {
    private static final Logger log = LoggerFactory.getLogger(ClientListener.class);

    @Override
    protected void onAfterSave(AfterSaveEvent<Client> event) {
        log.info("After Client SaveEvent, client:{}", event.getEntity());
    }
}
