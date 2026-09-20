package com.pacal.share.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public final class IPUtils {

    private static final String UNKNOWN = "unknown";

    public static String getIpAddress(HttpServletRequest request) {
        // cloudflare
        String ip = request.getHeader("CF-Connecting-IP");
        if (isInvalid(ip)) {
            // ali yun 负载
            ip = request.getHeader("X-Forwarded-For");
        }
        if (isInvalid(ip)) {
            // gateway nginx
            ip = request.getHeader("X-Real-IP");
        }
        if (isInvalid(ip)) {
            // apache + webLogic
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isInvalid(ip)) {
            // apache + webLogic
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isInvalid(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isInvalid(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isInvalid(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(','));
        }

        log.debug("最终获取真实IP地址为：{}", ip);
        return ip;
    }

    private static boolean isInvalid(String input) {
        return StringUtils.isEmpty(input) || UNKNOWN.equalsIgnoreCase(input);
    }
}
