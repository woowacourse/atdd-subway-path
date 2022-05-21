package wooteco.subway.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAop {

    private static final Logger logger = LoggerFactory.getLogger(LoggerAop.class);

    @Pointcut("execution(* wooteco.subway.ui.*Controller.*(..))")
    private void logTarget() {
    }

    @Before("logTarget()")
    public void printRequest(JoinPoint joinPoint) {
        logger.info("###### request ######");
        Object[] arguments = joinPoint.getArgs();

        if (arguments == null || arguments.length == 0) {
            logger.info("no request");
            return;
        }

        for (Object object : arguments) {
            logger.info(object.getClass().getSimpleName() + ": " + object);
        }
    }

    @AfterReturning(pointcut = "logTarget()", returning = "object")
    public void printResponse(Object object) {
        logger.info("###### response ######");
        if (object == null) {
            logger.info("no response");
            return;
        }
        logger.info(object.toString());
    }

    @Around("logTarget()")
    public Object printExecuteTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long t1 = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long t2 = System.currentTimeMillis();
            String className = joinPoint.getSignature().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();
            logger.info(String.format("%s.%s() 실행 시간: %d mls", className, methodName, (t2 - t1)));
        }
    }
}
