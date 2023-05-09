package aop.cglib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CglibDemo {
    private static final Logger logger = LoggerFactory.getLogger(CglibDemo.class);

    public static void main(String[] args) {
        var myClass =
                new Ioc()
                        .createMyClass(
                                (method, methodArgs) ->
                                        logger.info(
                                                "invoking method:{}, args:{}", method, methodArgs));
        ///////////////////////////

        myClass.secureAccess("Security Param");
    }
}
