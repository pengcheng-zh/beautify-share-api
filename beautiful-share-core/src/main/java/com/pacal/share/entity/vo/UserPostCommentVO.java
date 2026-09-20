package com.pacal.share.entity.vo;

import com.pacal.share.entity.po.UserPostCommentPO;
import lombok.Data;

@Data
public class UserPostCommentVO {
    private Integer id;
    private Integer userId;
    private String username;
    private String avatar;
    private Integer postId;
    private String content;
    private String status;
    private String auditReason;
    private String auditTime;
    private String createTime;

    public static UserPostCommentVO from(UserPostCommentPO po) {
        UserPostCommentVO vo = new UserPostCommentVO();
        vo.setId( po.getId() );
        vo.setUserId( po.getUserId() );
        vo.setPostId( po.getPostId() );
        vo.setContent( po.getContent() );
        vo.setStatus( po.getStatus() );
        vo.setAuditReason( po.getAuditReason() );
        vo.setAuditTime( po.getAuditTime() );
        vo.setCreateTime( po.getCreateTime() );
        return vo;
    }
}