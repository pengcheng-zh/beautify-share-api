package com.pacal.share.aop;


import com.pacal.share.common.ErrorCode;
import com.pacal.share.common.PacalException;
import com.pacal.share.context.PacalContextHolder;
import com.pacal.share.service.RedisService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
@Order(9)
public class NoRepeatAspect {
    private static final int REPEAT_LOCK_TIME = 1;

    @Resource
    RedisService redisService;

    /**
     * 防止重复提交的请求,请求之前的逻辑
     * 对 用户id + 请求参数的hashcode 加锁
     *
     * @param joinPoint JoinPoint
     */
    @Before("@annotation(com.pacal.share.annotation.NoRepeated)")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String action = joinPoint.getSignature().getName();
        String actionKey = getRedisKey( action, args );
        boolean success = redisService.setNx( actionKey, "1", REPEAT_LOCK_TIME );
        if ( success  ){
            return;
        }
        log.error( "repeat submit: {}", joinPoint.toLongString() );
        throw new PacalException( ErrorCode.REPEAT_SUBMIT );
    }

    /**
     * 防止重复提交的请求,请求之后的逻辑
     * 释放锁
     *
     * @param joinPoint 获取请求Request
     */
    @AfterReturning("@annotation(com.pacal.share.annotation.NoRepeated)")
    public void afterReturning(JoinPoint joinPoint) {
        String action = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String actionKey = getRedisKey( action, args );
        // redisService.delete( actionKey );
    }

    /**
     * 获取防止相同请求重复提交的redis锁的key
     *
     * @param objects 请求参数
     * @return key redis锁的key
     */
    private String getRedisKey(String action, Object[] objects) {
        String key = "";
        if ( objects.length > 0 ) {
            key += String.valueOf( Arrays.hashCode( objects ) );
        }
        String uid = PacalContextHolder.getUserId();
        return String.format( "ua:%s:%s:%s", uid, action, key );
    }
}
