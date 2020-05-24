package com.deliverypp.aop;

import com.deliverypp.controllers.OrderController;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class DeliveryppLogginAspect {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Pointcut("@annotation(com.deliverypp.util.DeliveryppLoggin)")
    public void loggingOperation() {}

    @Before("execution(* com.deliverypp.services..*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("aop Before -> " + joinPoint.getSignature().getName() + " params: " + Arrays.toString(joinPoint.getArgs()));
    }

    /*
    @After("execution(* com.deliverypp.services..*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("aop After -> " + joinPoint.getSignature().getName() + " params: " + Arrays.toString(joinPoint.getArgs()));
    }
*/

    @AfterReturning(
            pointcut = "loggingOperation()",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("aop AfterReturning -> method: {}, result: {}", joinPoint.getSignature().getName(), result);
    }

}
