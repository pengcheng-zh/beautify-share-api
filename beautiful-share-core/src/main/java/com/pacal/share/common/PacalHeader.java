package com.pacal.share.common;

import lombok.Data;

@Data
public class PacalHeader {
    private String platform;

    private String version;

    private String userAgent;

    private String authorization;

    private String userId;
    private String ip;
}
