package cohort_65.java.forumservice.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class MetricsAspect {

    private final MonitoringService monitoringService;

    public MetricsAspect(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }


    @Around("execution(* cohort_65.java.forumservice..*(..))")
    public Object measureMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();

        long start = System.currentTimeMillis();

        boolean success = true;

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            success = false;
            throw e;
        } finally {

            long timeTaken = System.currentTimeMillis() - start;
            monitoringService.recordMetric(methodName, timeTaken, success);
        }
    }
}
