package com.pacal.share.utils;


import com.pacal.share.common.Constants;
import com.pacal.share.common.PacalHeader;
import com.pacal.share.context.PacalContext;
import com.pacal.share.context.PacalContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public final class RequestUtil {

    private static final ThreadLocal<PacalHeader> headerHolder = new NamedThreadLocal<>( "pacalHeader" );

    public static void resetHeader() {
        headerHolder.remove();
    }

    public static void setHeader(@Nullable PacalHeader header) {
        if (Objects.isNull( header )) {
            resetHeader();
        } else {
            headerHolder.set( header );
        }
    }
    
    public static PacalHeader getHeader() {
        PacalHeader header = headerHolder.get();
        if ( Objects.isNull( header ) ) {
            header = getPacalHeader();
            setHeader( header );
        }
        return header;
    }

    private static PacalHeader getPacalHeader() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull( RequestContextHolder.getRequestAttributes() )).getRequest();

        String userId = "";
        String ipAddress = "";
        PacalContext context = PacalContextHolder.getContext();
        if ( Objects.nonNull( context ) ) {
            userId = context.getUserId();
            ipAddress = context.getIpAddress();
        }

        PacalHeader header = new PacalHeader();
        header.setAuthorization( request.getHeader( Constants.HEADER_AUTHORIZATION ) );
        header.setPlatform( request.getHeader( "platform" ) );
        header.setUserAgent( request.getHeader( "User-Agent" ) );
        header.setVersion( request.getHeader( "version" ) );
        header.setIp( ipAddress );
        header.setUserId( userId );
        return header;
    }

    public static String getUserId() {
        PacalContext context = PacalContextHolder.getContext();
        return Objects.isNull( context ) ? "" : context.getUserId();
    }

    public static String getRoleId() {
        PacalContext context = PacalContextHolder.getContext();
        return Objects.isNull( context ) ? "" : context.getRoleName();
    }

    public static Integer getUserIdInt() {
        String userId = getUserId();
        if ( StringUtils.isNumeric( userId ) ) {
            return Integer.parseInt( userId );
        }
        return 0;
    }

    public static Map<String, String> getHeaderMap(PacalHeader header) {
        if ( Objects.isNull( header ) ) {
            header = getHeader();
        }
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put( "authorization", header.getAuthorization() );
        headerMap.put( "platform", header.getPlatform() );
        headerMap.put( "User-Agent", header.getUserAgent() );
        headerMap.put( "version", header.getVersion() );
        headerMap.put( "user_id", header.getUserId() );
        headerMap.put( "ip", header.getIp() );
        return headerMap;
    }

    public static String getRequestBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = request.getReader();
            String line;
            while ( (line = bufferedReader.readLine()) != null ) {
                stringBuilder.append( line );
            }
        } catch ( Exception ex ) {
            log.error( "getRequestBody error: ", ex );
        } finally {
            if ( bufferedReader != null ) {
                try {
                    bufferedReader.close();
                } catch ( Exception ex ) {
                    log.error( "getRequestBody close error:", ex );
                }
            }
        }
        return stringBuilder.toString();
    }
}
