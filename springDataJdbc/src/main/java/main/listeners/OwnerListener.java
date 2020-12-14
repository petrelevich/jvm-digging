package main.listeners;

import main.demo.model.Owner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.relational.core.mapping.event.AbstractRelationalEventListener;
import org.springframework.data.relational.core.mapping.event.AfterSaveEvent;

public class OwnerListener extends AbstractRelationalEventListener<Owner> {
    private static final Logger log = LoggerFactory.getLogger(OwnerListener.class);

    @Override
    protected void onAfterSave(AfterSaveEvent<Owner> event) {
        log.info("After Owner SaveEvent, owner:{}", event.getEntity());
    }
}