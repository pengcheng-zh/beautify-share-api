package com.pacal.share.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "wechat.xch")
@Data
public class WxMiaProperties {
    private String appId;
    private String appSecret;
    private String token;
    private String aesKey;
    private String msgDataFormat;
}
