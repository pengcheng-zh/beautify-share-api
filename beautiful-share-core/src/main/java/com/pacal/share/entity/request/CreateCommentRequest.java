package com.pacal.share.entity.request;

import lombok.Data;

/**
 * 评论请求
 */
@Data
public class CreateCommentRequest {
    private Integer postId;
    private String content;
}