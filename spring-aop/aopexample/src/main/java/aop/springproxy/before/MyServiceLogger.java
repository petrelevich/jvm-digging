package aop.springproxy.before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class MyServiceLogger implements MyService {
    private static final Logger logger = LoggerFactory.getLogger(MyServiceLogger.class);

    private final MyService self;

    public MyServiceLogger(@Lazy MyService myService) {
        logger.info("Lazy myService.class:{}", myService.getClass());
        this.self = myService;
    }

    @Loggable
    public void action1(String param) {
        logger.info("    action1, param:{}", param);
        action2("not logged");
        self.action2("!!!self logged");
    }

    @Loggable
    public void action2(String param) {
        logger.info("    action2, param:{}", param);
    }
}
