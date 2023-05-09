package aop.springproxy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BeforeActionLogging {
    private static final Logger logger = LoggerFactory.getLogger(BeforeActionLogging.class);

    @Before("execution(* aop.springproxy.MyClass.secureAccess(..))")
    public void action(JoinPoint joinPoint) {
        logger.info("proxy class:{}", joinPoint.getThis().getClass());
        logger.info("invoking method:{}, args:{}", joinPoint.getSignature(), joinPoint.getArgs());
    }
}
