package com.example.hospital.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserLoggingAspect {
    // log trước khi thao tác vs user
    @Before("execution(* com.example.hospital.service.impl.UserServiceImpl(..))")
    public void logBeforeUser(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("---- Log trước khi thao tấc vs user [UserLoggingAspect] ----");
        System.out.println("Class thực thi: " + joinPoint.getTarget().getClass().getSimpleName());
        System.out.println("Method được gọi: " + methodName);
    }

}
