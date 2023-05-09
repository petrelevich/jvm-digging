package aop.springproxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MyClass implements MyClassInterface {
    private static final Logger logger = LoggerFactory.getLogger(MyClass.class);

    @Override
    public void secureAccess(String param) {
        logger.info("class:{}, secureAccess, param:{}", this.getClass(), param);
    }

    @Override
    public String toString() {
        return "MyClass{}";
    }
}
