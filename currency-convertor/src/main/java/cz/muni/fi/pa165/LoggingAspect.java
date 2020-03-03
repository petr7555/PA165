package cz.muni.fi.pa165;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Petr Janik 485122
 * @since 03.03.2020
 */
@Aspect
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private long startTime;
    private long endTime;

    @Pointcut("execution(* cz.muni.fi.pa165.currency.CurrencyConvertorImpl+.*(..))")
    public void loggable() {
    }


    @Before("loggable()")
    public void beforeMethodStatistics(JoinPoint jp) {
        startTime = System.nanoTime();
    }

    @After("loggable()")
    public void afterMethodStatistics() {
        endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        logger.info("elapsed {} nanoseconds", timeElapsed);
    }
}
