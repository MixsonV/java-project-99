package hexlet.code.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {
    //private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLoggingAspect.class);
    @AfterThrowing(pointcut = "within(@org.springframework.stereotype.Controller *)", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.info("Exception in method {}: {}", joinPoint.getSignature(), ex.getMessage(), ex);
        //LOGGER.error("Exception in method {}: {}", joinPoint.getSignature(), ex.getMessage(), ex);
    }
}
