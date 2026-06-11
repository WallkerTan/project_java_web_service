package com.example.hospital.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    // đo thời gian thực thi các phương thức trong service
    @Around("execution(* com.example.hospital.service.impl.*.*(..))")
    public Object LogExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Long startTime = System.currentTimeMillis();
        Object res = proceedingJoinPoint.proceed();
        Long timeTaken = System.currentTimeMillis() - startTime;

        try {
            System.out.println("---Transaction Around log time [LogAspect]---");
            System.out.println("phương thức: " + proceedingJoinPoint.getSignature().getName());
            System.out.println("thời gian: " + timeTaken);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return res;
    }


    // đo thời gian thực thi các phương thức đưuọc đánh dấu
    @Around("execution(* com.example.s7ex5aspect.annotation.TrackTime)")
    public Object LogAnnotationExecutionTime(ProceedingJoinPoint proceedingJoinPoint)
            throws Throwable {

        Long startTime = System.currentTimeMillis();
        Object res = proceedingJoinPoint.proceed();
        Long timeTaken = System.currentTimeMillis() - startTime;

        try {
            System.out.println("---Transaction Around log time [LogAspect annotation]---");
            System.out.println("phương thức: " + proceedingJoinPoint.getSignature().getName());
            System.out.println("thời gian: " + timeTaken);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return res;
    }

}
