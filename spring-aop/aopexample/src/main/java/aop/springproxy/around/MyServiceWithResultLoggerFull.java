package aop.springproxy.around;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MyServiceWithResultLoggerFull implements MyServiceWithResult {
    private static final Logger logger =
            LoggerFactory.getLogger(MyServiceWithResultLoggerFull.class);

    @LoggableFull
    public String action(String param) {
        logger.info("    action, param:{}", param);
        return param.toUpperCase();
    }
}
