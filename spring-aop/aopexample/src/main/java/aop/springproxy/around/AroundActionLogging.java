package aop.springproxy.around;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AroundActionLogging {
    private static final Logger logger = LoggerFactory.getLogger(AroundActionLogging.class);

    @Around("@annotation(aop.springproxy.around.LoggableFull)")
    public Object action(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info(
                "before invoking method:{}, args:{}",
                joinPoint.getSignature(),
                joinPoint.getArgs());

        var result = joinPoint.proceed();

        logger.info("after invoking method:{}, result:{}", joinPoint.getSignature(), result);
        return result;
    }
}
