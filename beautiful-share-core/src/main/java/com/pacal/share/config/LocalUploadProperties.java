package com.pacal.share.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "upload.local")
public class LocalUploadProperties {
    private String directory;
    private String publicBaseUrl;
}
