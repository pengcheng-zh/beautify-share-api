package com.pacal.share.entity.po;

import lombok.Data;

/**
 * 用户发表的文章(对应 user_post 表)
 */
@Data
public class UserPostPO {
    private Integer id;
    /** 作者用户id */
    private Integer userId;
    /** 文章内容 */
    private String content;
    /** 图片URL列表(逗号分隔) */
    private String pictures;
    /** 位置文字描述 */
    private String location;
    /** 纬度 */
    private String latitude;
    /** 经度 */
    private String longitude;
    /** 语音URL */
    private String voice;
    private Integer duration;
    /** 状态: A-待审核 P-已通过 R-已拒绝 */
    private String status;
    /** 审核人 */
    private Integer auditUserId;
    /** 审核备注 */
    private String auditReason;
    private String auditTime;

    private Integer likeCount;
    private Integer commentCount;
    private String createTime;
    private String updateTime;
}