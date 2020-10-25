package ru.petrelevich.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import ru.petrelevich.bean.MyBean;

@MyBean
public class SomeAction {
    private static final Logger log = LoggerFactory.getLogger(SomeAction.class);

    private final AdditionalAction additionalAction;

    public SomeAction(AdditionalAction additionalAction) {
        this.additionalAction = additionalAction;
    }

    public void doAction() {
        log.info("do some action");
        additionalAction.someAction();
    }

    @Bean
    public void method1() {
        log.info("action 1");
    }

    @Bean
    public void method2() {
        log.info("action 2");
    }

    @Bean
    public void method3() {
        log.info("action 3");
    }
}
