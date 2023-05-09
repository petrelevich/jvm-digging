package aop.springproxy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoBean {
    private static final Logger logger = LoggerFactory.getLogger(DemoBean.class);
    private final DemoBeanArgument demoBeanArgument;

    public DemoBean(DemoBeanArgument demoBeanArgument) {
        this.demoBeanArgument = demoBeanArgument;
        print();
    }

    void print() {
        var hiMsg = demoBeanArgument.hi();
        logger.info("{}, demoBeanArgument:{}", hiMsg, demoBeanArgument);
    }
}
