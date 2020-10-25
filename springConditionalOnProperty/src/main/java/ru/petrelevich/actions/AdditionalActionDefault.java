package ru.petrelevich.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import ru.petrelevich.bean.MyBean;

@MyBean
@ConditionalOnProperty(prefix = "example", name = "UseAdditionalAction", matchIfMissing = true, havingValue = "false")
public class AdditionalActionDefault implements AdditionalAction {
    private static final Logger log = LoggerFactory.getLogger(AdditionalActionDefault.class);

    public AdditionalActionDefault() {
        log.info("AdditionalActionDefault constructor");
    }

    @Override
    public void someAction() {
        log.info("some additional action. Default");
    }
}
