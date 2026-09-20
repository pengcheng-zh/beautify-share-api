package com.pacal.share.entity.po;

import lombok.Data;

/**
 * 用户对文章的收藏(对应 user_post_favorite 表)
 */
@Data
public class UserPostFavoritePO {
    private Integer id;
    /** 收藏用户id */
    private Integer userId;
    /** 被收藏的文章id */
    private Integer postId;
    private String status;
    private String createTime;
}
