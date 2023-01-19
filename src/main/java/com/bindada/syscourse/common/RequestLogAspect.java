package com.bindada.syscourse.common;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求日志打印
 */
@Component
@Aspect
public class RequestLogAspect {
    private static final Logger log = LoggerFactory.getLogger(RequestLogAspect.class);

    /**
     * 切入点
     */
    @Pointcut("execution(* com.bindada.syscourse.controller..*(..)) ")
    public void entryPoint() {
        // 无需内容
    }

    /**
     * 打印请求信息
     * @param joinPoint
     */
    @Before("entryPoint()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] parameters = joinPoint.getArgs();
            log.info("==================接口请求before日志开始==================");
            log.info("URL:"+request.getRequestURL().toString()+"\n"
                    +"IP:"+request.getRemoteAddr()+"\n"
                    +"HTTP Method:"+request.getMethod()+"\n"
                    +"类名:" + className+"\n"
                    + "方法名:" + methodName+"\n"
                    + "请求参数:" + JSON.toJSONString(parameters));
            log.info("==================接口请求before日志结束==================");
        } catch (Throwable e) {
            log.info("around " + joinPoint + " with exception : " + e.getMessage());
        }

    }

    /**
     * 打印请求及响应信息及耗时
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("entryPoint()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        String className = joinPoint.getTarget().getClass().getName(); //类名
        String methodName = joinPoint.getSignature().getName(); //请求方法名

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed(); //该方法会去调用一次当前请求的方法，会这里让进程进入到相应请求的业务方法里面
        long time = System.currentTimeMillis() - startTime;

        log.info("==================接口返回around日志开始==================");
         log.info(
                 "类名:" + className + "\n"
                         + "方法名:" + methodName + "\n"
                         + "返回结果:" + JSON.toJSONString(result) + "\n"
                         + "方法执行耗时:" + time + "ms"
         );
        log.info("==================接口返回around日志结束==================");

        return result;
    }

    /**
     * 异常抛出之后的打印
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "entryPoint()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        // 通过request获取登陆用户信息
        // HttpServletRequest request = ((ServletRequestAttributes)
        // RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] parameters = joinPoint.getArgs();

            log.info("异常方法:" + className + "." + methodName + "();参数:" + JSON.toJSONString(parameters));
            log.info("异常信息:" + e.getMessage());
        } catch (Exception ex) {
            log.error("异常信息:{}", ex.getMessage());
        }
    }
}
