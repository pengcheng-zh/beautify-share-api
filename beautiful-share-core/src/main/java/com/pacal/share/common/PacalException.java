package com.pacal.share.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class PacalException extends RuntimeException implements Serializable {
    @Serial
    private static final long serialVersionUID = 4786241555414600788L;

    private ErrorCode errorCode;

    private String[] message_param = null;

    public PacalException(ErrorCode code) {
        this(code, (String) null);
    }

    public PacalException(ErrorCode code, String... message_param) {
        super();
        this.errorCode = code;
        this.message_param = message_param;
    }
}
