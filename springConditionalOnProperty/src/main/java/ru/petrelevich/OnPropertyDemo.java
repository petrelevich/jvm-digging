package ru.petrelevich;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import ru.petrelevich.bean.MyBean;
import ru.petrelevich.actions.SomeAction;


@SpringBootApplication
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyBean.class))
public class OnPropertyDemo {
    private static final Logger log = LoggerFactory.getLogger(OnPropertyDemo.class);

    public static void main(String[] args) {
        var context = SpringApplication.run(OnPropertyDemo.class, args);

        var action = context.getBean(SomeAction.class);
        action.doAction();
    }

    @Bean
    public void methodDemo1() {
        log.info("methodDemo 1");
    }

    @Bean
    public void methodDemo2() {
        log.info("methodDemo 2");
    }

    @Bean
    public void methodDemo3() {
        log.info("methodDemo 3");
    }

}
