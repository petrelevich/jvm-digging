package aop.springproxy.before;

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

    @Before("@annotation(aop.springproxy.before.Loggable)")
    public void action(JoinPoint joinPoint) {
        logger.info("proxy class:{}", joinPoint.getThis().getClass());
        logger.info("invoking method:{}, args:{}", joinPoint.getSignature(), joinPoint.getArgs());
    }
}
