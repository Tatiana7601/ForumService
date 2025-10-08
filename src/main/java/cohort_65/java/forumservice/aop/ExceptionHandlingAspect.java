package cohort_65.java.forumservice.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingAspect.class);

    private final MonitoringService monitoringService;

    public ExceptionHandlingAspect(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @AfterThrowing(pointcut = "execution(* cohort_65.java.forumservice..*(..))", throwing = "ex")
    public void handleException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();

        logger.error("❌ Exception in method: {} | Exception type: {} | Message: {}",
                methodName,
                ex.getClass().getSimpleName(),
                ex.getMessage());

        monitoringService.recordError(methodName);
    }

}

