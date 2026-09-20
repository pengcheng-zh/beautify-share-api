package com.pacal.share.entity.request;

import lombok.Data;

import java.util.List;

/**
 * 发表文章请求
 */
@Data
public class CreatePostRequest {
    private Integer id;
    private String content;
    private List<String> pictures;
    private String location;
    private String latitude;
    private String longitude;
    private String voice;
    private Integer duration;
}