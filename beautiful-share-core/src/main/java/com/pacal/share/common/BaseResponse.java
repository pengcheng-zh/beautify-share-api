package com.pacal.share.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> implements Serializable {

    private String message_id;

    private String message;

    private Boolean result;

    private T object;

    public static <T> BaseResponse<T> SUCCESS() {
        return success(null);
    }

    public static <T> BaseResponse<T> success() {
        return success(null);
    }

    public static <T> BaseResponse<T> success(T context) {
        return send( ErrorCode.SUCCESS, true, context);
    }

    public static <T> BaseResponse<T> fail(ErrorCode code) {
        return fail(code, null);
    }

    public static <T> BaseResponse<T> fail(ErrorCode code, Object[] args) {
        if ( Objects.isNull( args ) ) {
            return send( code, false, null );
        } else {
            return send( code, false, null, args );
        }
    }

    public static <T> BaseResponse<T> send(ErrorCode code, boolean result, T body, Object[] args) {
        return new BaseResponse<>( code.getCode(), String.format( code.getMessage(), args ), result, body );
    }
    public static <T> BaseResponse<T> send(ErrorCode code, boolean result, T body) {
        return new BaseResponse<>( code.getCode(), code.getMessage(), result, body );
    }

}

