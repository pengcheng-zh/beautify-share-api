package com.pacal.share.context;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pacal.share.common.Constants;
import com.pacal.share.utils.IPUtils;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class PacalRequestListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent requestEvent) {
        if ( !(requestEvent.getServletRequest() instanceof HttpServletRequest request) ) {
            throw new IllegalArgumentException(
                    "Request is not an HttpServletRequest: " + requestEvent.getServletRequest() );
        }

        String ipAddress = IPUtils.getIpAddress( request );
        PacalContext pacalContext = new PacalContext();
        pacalContext.setIpAddress( ipAddress );

        String tokenStr = request.getHeader( Constants.HEADER_AUTHORIZATION );
        log.info( "request header token: {} {}", tokenStr, ipAddress );
        String tokenStart = "Bearer ";

        if ( StringUtils.isNotBlank( tokenStr ) && tokenStr.startsWith( tokenStart ) ) {
            tokenStr = tokenStr.substring( tokenStart.length() - 1 ).trim();
            try {
                DecodedJWT decodedJWT = JWT.decode( tokenStr );
                pacalContext.setDecodedJWT( decodedJWT );
                pacalContext.setUserId( CollectionUtils.isEmpty( decodedJWT.getAudience() ) ? "" : decodedJWT.getAudience().get( 0 ) );
                // role_id 仅用于缓存,具体鉴权由 TokenAspect 从数据库获取最新值
                String roleId = decodedJWT.getClaim( "role_id" ).asString();
                pacalContext.setRoleName( roleId == null ? "" : roleId );
            } catch ( Exception e ) {
                // 不在 Listener 中抛出异常,异常处理留给 TokenAspect
                log.error( "token Invalid - {}, error: {}", tokenStr, e.getMessage() );
            }
        }
        PacalContextHolder.setContext( pacalContext );
    }

    @Override
    public void requestDestroyed(ServletRequestEvent requestEvent) {
        PacalContextHolder.resetContext();
    }
}