package ru.demo.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassParamDemo {
    private static final Logger log = LoggerFactory.getLogger(PassParamDemo.class);

    private PassParamDemo() {}

    static void main() {
        new PassParamDemo().methodA("paramValue");
    }

    private void methodA(String param) {
        log.info("methodA actions");
        methodB(param);
    }

    private void methodB(String param) {
        log.info("methodB actions");
        methodC(param);
    }

    private void methodC(String param) {
        log.info("use param:{}", param);
    }
}
