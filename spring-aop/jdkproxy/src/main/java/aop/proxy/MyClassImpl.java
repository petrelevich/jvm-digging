package aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClassImpl implements MyClassInterface {
    private static final Logger logger = LoggerFactory.getLogger(MyClassImpl.class);

    @Override
    public void secureAccess(String param) {
        logger.info("secureAccess, param:{}", param);
    }

    @Override
    public String toString() {
        return "MyClassImpl{}";
    }
}
