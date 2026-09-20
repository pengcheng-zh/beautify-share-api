package com.pacal.share.entity.dto;

import lombok.Data;

import java.io.InputStream;

@Data
public class StorageDTO {
    private String objectName;
    private String contentType;
    private Long contentLength;
    private InputStream inputStream;
}