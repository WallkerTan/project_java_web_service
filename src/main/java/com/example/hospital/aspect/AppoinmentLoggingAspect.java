package com.example.hospital.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
// log khi giao dịch
public class AppoinmentLoggingAspect {

    // log trước khi giao dịch
    @Before("execution(* com.example.hospital.service.impl.AppoinmentServiceImpl.*(..))")
    public void logBeforeTransaction(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out
                .println("----Log trước khi thao tấc vs Appoinment [AppoinmentLoggingAspect]----");
        System.out.println("Class thực thi: " + joinPoint.getTarget().getClass().getSimpleName());
        System.out.println("Method được gọi: " + methodName);
    }

}
