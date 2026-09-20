package com.pacal.share.aop;

import com.alibaba.fastjson2.JSON;
import com.pacal.share.common.Constants;
import com.pacal.share.utils.IPUtils;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
@Order(1)
public class RequestLogAspect {
    public RequestLogAspect() {

    }
    @Around( "execution(public * com.pacal..controller..*.*(..))" )
    public Object around(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if ( attrs != null ) {
            HttpServletRequest request = attrs.getRequest();
            String method = request.getMethod();
            String url = request.getRequestURI();
            String ip = IPUtils.getIpAddress( request );
            String query = request.getQueryString();
            String authorization = request.getHeader( Constants.HEADER_AUTHORIZATION );
            
            // 输出请求基本信息
            log.info("Request: {} {} {} {} {}", method, url, ip, query, authorization);
        }
        safeLogParams( point.getArgs() );

        return point.proceed();
    }

    private void safeLogParams(Object[] args) {
        List<Object> safeArgs = Arrays.stream(args)
                .filter(arg -> !(arg instanceof ServletRequest || arg instanceof ServletResponse || arg instanceof MultipartFile))
                .collect( Collectors.toList());
        try {
            String logStr = JSON.toJSONString( safeArgs );

            log.info("Request Params: {}", logStr);
        } catch (Exception ex) {
            //忽略异常,交给filter继续尝试 Log params
            log.info("Request Params error: ", ex);
        }
    }
}
