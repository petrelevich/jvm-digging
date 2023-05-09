package aop.cglib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClass {
    private static final Logger logger = LoggerFactory.getLogger(MyClass.class);

    public void secureAccess(String param) {
        logger.info("secureAccess, param:{}", param);
    }

    @Override
    public String toString() {
        return "MyClassImpl{}";
    }
}
