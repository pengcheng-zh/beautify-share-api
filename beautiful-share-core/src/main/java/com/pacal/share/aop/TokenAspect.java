package com.pacal.share.aop;

import com.pacal.share.annotation.NeedAdmin;
import com.pacal.share.annotation.NeedLogin;
import com.pacal.share.annotation.NeedNormalAdmin;
import com.pacal.share.common.ErrorCode;
import com.pacal.share.common.PacalException;
import com.pacal.share.context.PacalContext;
import com.pacal.share.context.PacalContextHolder;
import com.pacal.share.dao.UserDao;
import com.pacal.share.entity.po.UserPO;
import com.pacal.share.enumm.UserRoleEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
@Component
@Aspect
@Order(2)
public class TokenAspect {

    @Resource
    UserDao userDao;

    @Before("execution(public * com.pacal..controller..*.*(..))")
    public void before(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        boolean needLogin = method.isAnnotationPresent( NeedLogin.class );
        boolean needAdmin = method.isAnnotationPresent( NeedAdmin.class );
        boolean needNormalAdmin = method.isAnnotationPresent( NeedNormalAdmin.class );
        if ( needLogin || needAdmin || needNormalAdmin ) {
            checkToken( needAdmin, needNormalAdmin );
        }
    }

    public void checkToken(boolean needAdmin, boolean needNormalAdmin) {
        //是否为空
        PacalContext pacalContext = PacalContextHolder.getContext();
        if ( Objects.isNull( pacalContext ) || StringUtils.isEmpty( pacalContext.getUserId() ) || Objects.isNull( pacalContext.getDecodedJWT() ) ) {
            throw new PacalException( ErrorCode.TOKEN_IS_INVALID );
        }

        //是否登录
        if ( !StringUtils.isNumeric( pacalContext.getUserId() ) ) {
            throw new PacalException( ErrorCode.NO_LOGIN );
        }

        //是否过期
        long expTime = pacalContext.getDecodedJWT().getExpiresAtAsInstant().getEpochSecond();
        long nowTime = System.currentTimeMillis() / 1000;
        if ( nowTime > expTime ) {
            throw new PacalException( ErrorCode.TOKEN_EXPIRED );
        }

        // 检查用户是否存在
        UserPO userPO = userDao.getUser( Integer.parseInt( pacalContext.getUserId() ) );
        if ( Objects.isNull( userPO ) ) {
            throw new PacalException( ErrorCode.NO_RIGHT );
        }

        // 不需要检查管理员权限
        if ( !needAdmin && !needNormalAdmin ) {
            return;
        }

        if ( needAdmin && !UserRoleEnum.ADMIN.roleId.equals( userPO.getRoleId() ) ) {
            throw new PacalException( ErrorCode.NO_RIGHT );
        }

        // 普通管理员及以上(超管)可访问
        if ( UserRoleEnum.NORMAL.roleId.equals( userPO.getRoleId() ) ) {
            throw new PacalException( ErrorCode.NO_RIGHT );
        }
    }
}