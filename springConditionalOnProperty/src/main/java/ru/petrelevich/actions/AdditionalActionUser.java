package ru.petrelevich.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import ru.petrelevich.bean.MyBean;

@MyBean
@ConditionalOnProperty(prefix = "example", name = "UseAdditionalAction")
public class AdditionalActionUser implements AdditionalAction {
    private static final Logger log = LoggerFactory.getLogger(AdditionalActionUser.class);

    @Override
    public void someAction() {
        log.info("some additional action. USER");
    }
}
