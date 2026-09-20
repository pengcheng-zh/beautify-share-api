package com.pacal.share.context;

import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;

import java.util.Objects;

public class PacalContextHolder {

    private static final ThreadLocal<PacalContext> pacalContextHolder = new NamedThreadLocal<>( "PacalContext" );

    public static void resetContext() {
        pacalContextHolder.remove();
    }

    public static void setContext(@Nullable PacalContext pacalContext) {
        if ( pacalContext == null ) {
            resetContext();
        } else {
            pacalContextHolder.set( pacalContext );
        }
    }

    @Nullable
    public static PacalContext getContext() {
        return pacalContextHolder.get();
    }

    public static String getIpAddress() {
        return Objects.requireNonNull( getContext() ).getIpAddress();
    }

    public static String getUserId() {
        return Objects.requireNonNull( getContext() ).getUserId();
    }

    public static String getRoleName() {
        return Objects.requireNonNull( getContext() ).getRoleName();
    }
}
