package com.pacal.share.entity.po;

import lombok.Data;

/**
 * 用户对文章的点赞(对应 user_post_like 表)
 */
@Data
public class UserPostLikePO {
    private Integer id;
    /** 点赞用户id */
    private Integer userId;
    /** 被点赞的文章id */
    private Integer postId;
    private String status;
    private String createTime;
}
