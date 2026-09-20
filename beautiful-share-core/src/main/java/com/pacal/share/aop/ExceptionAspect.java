package com.pacal.share.aop;

import com.pacal.share.common.BaseResponse;
import com.pacal.share.common.ErrorCode;
import com.pacal.share.common.PacalException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.sql.SQLException;

@Component
@Aspect
@Slf4j
@Order(2)
public class ExceptionAspect {
    @Resource
    private Environment environment;

    @Around("execution(public * com.pacal..controller..*.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object responseObject = null;
        try {
            responseObject = point.proceed();
        } catch ( PacalException se) {
            BaseResponse<Object> response = BaseResponse.fail(se.getErrorCode(), se.getMessage_param());
            log.warn("PacalException Error messageId: {}, message: {}", se.getErrorCode().getCode(), response.getMessage());
            return response;
        } catch (Exception ex) {
            return systemExceptionHandler(ex, point);
        }
        return responseObject;
    }

    private BaseResponse<String> systemExceptionHandler(Exception ex, ProceedingJoinPoint point) {
        String exceptionRefKey = "eKey:" + System.currentTimeMillis();
        log.error("System Exception Aspect - iForumException - Exception Ref Key : {} ", exceptionRefKey);
        log.error("Exception:", ex);

        // 异步发送异常信息邮件

        String alertInfo = "请联系管理员, 谢谢！";
        return switch ( ex ) {
            case ConnectTimeoutException connectTimeoutException ->
                    BaseResponse.send( ErrorCode.CONNECT_TIME_OUT, false, alertInfo );
            case NoHttpResponseException noHttpResponseException ->
                    BaseResponse.send( ErrorCode.HTTP_CONNECT_EXCEPTION, false, alertInfo );
            case SQLException ignored -> BaseResponse.send( ErrorCode.SQL_EXCEPTION, false, alertInfo );
            case MaxUploadSizeExceededException maxUploadSizeExceededException ->
                    BaseResponse.send( ErrorCode.MAX_FILE_SIZE, false, alertInfo );
            case null, default -> BaseResponse.send( ErrorCode.SYSTEM_ERROR, false, alertInfo );
        };
    }
}
