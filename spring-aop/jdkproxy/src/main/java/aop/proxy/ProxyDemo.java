package aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyDemo {
    private static final Logger logger = LoggerFactory.getLogger(ProxyDemo.class);

    public static void main(String[] args) {
        MyClassInterface myClass =
                new Ioc()
                        .createMyClass(
                                (method, methodArgs) ->
                                        logger.info(
                                                "invoking method:{}, args:{}", method, methodArgs));
        ///////////////////////////

        myClass.secureAccess("Security Param");
    }
}
