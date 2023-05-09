package aop.springproxy;

import aop.springproxy.around.MyServiceWithResult;
import aop.springproxy.before.MyServiceLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoAction implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DemoAction.class);
    private final MyServiceLogger myServiceLogger;
    private final MyServiceWithResult myServiceWithResult;

    public DemoAction(MyServiceLogger myServiceLogger, MyServiceWithResult myServiceWithResult) {
        this.myServiceLogger = myServiceLogger;
        this.myServiceWithResult = myServiceWithResult;
    }

    @Override
    public void run(String... args) {
        around();
        notAllLogged();
    }

    private void around() {
        var result = myServiceWithResult.action("low");
        logger.info("result:{}", result);
    }

    private void notAllLogged() {
        myServiceLogger.action1("logged");
        myServiceLogger.action2("logged");
    }
}
