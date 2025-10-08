package cohort_65.java.forumservice.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class.getName());

    @Around("execution(* cohort_65.java.forumservice..*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();

        Object[] args = joinPoint.getArgs();
        logger.info("Entering method: {} with args: {}", methodName, args);
        Object result = joinPoint.proceed();
        logger.info("Exiting method: {} with result: {}", methodName, result);
        return result;

    }
}
