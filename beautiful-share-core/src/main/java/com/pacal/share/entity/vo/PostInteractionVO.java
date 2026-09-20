package com.pacal.share.entity.vo;

import lombok.Data;

/**
 * 文章互动数据(点赞/收藏/评论)
 */
@Data
public class PostInteractionVO {
    private Integer postId;
    /** 点赞总数 */
    private int likeCount;
    /** 收藏总数 */
    private int favoriteCount;
    /** 已通过的评论总数 */
    private int commentCount;
    /** 当前访问者是否已点赞 */
    private boolean liked;
    /** 当前访问者是否已收藏 */
    private boolean favorited;
}
