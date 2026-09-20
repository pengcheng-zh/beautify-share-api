package com.pacal.share.entity.po;

import lombok.Data;

/**
 * 用户对文章的评论(对应 user_post_comment 表)
 */
@Data
public class UserPostCommentPO {
    private Integer id;
    /** 评论用户id */
    private Integer userId;
    /** 所属文章id */
    private Integer postId;
    /** 评论内容 */
    private String content;
    /** 状态: A-待审核 P-已通过 R-已拒绝 */
    private String status;
    /** 审核人 */
    private Integer auditUserId;
    /** 审核备注 */
    private String auditReason;
    private String auditTime;
    private String createTime;
    private String updateTime;
}