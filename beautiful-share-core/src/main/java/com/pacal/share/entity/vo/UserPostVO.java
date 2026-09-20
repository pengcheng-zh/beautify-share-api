package com.pacal.share.entity.vo;

import com.pacal.share.entity.po.UserPostPO;
import com.pacal.share.enumm.ApproveStatusEnum;
import com.pacal.share.utils.CommUtil;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
public class UserPostVO {
    private Integer id;
    private Integer userId;
    private String username;
    private String avatar;
    private String content;
    private List<String> pictures;
    private String location;
    private String latitude;
    private String longitude;
    private String voice;
    private String status;
    private String statusLabel;
    private String auditReason;
    private String auditTime;
    private String createTime;

    /** 点赞总数 */
    private int likeCount;
    /** 收藏总数 */
    private int favoriteCount;
    /** 已通过的评论总数 */
    private int commentCount;
    /** 当前访问者是否已点赞(未登录恒为 false) */
    private boolean liked;
    /** 当前访问者是否已收藏(未登录恒为 false) */
    private boolean favorited;

    /**携带最近三个点赞的用户信息 */
    private List<UserSimpleVO> likedUserList;

    public static UserPostVO from(UserPostPO po) {
        UserPostVO vo = new UserPostVO();
        vo.setId( po.getId() );
        vo.setUserId( po.getUserId() );
        vo.setContent( po.getContent() );
        vo.setPictures(CommUtil.toStrList(po.getPictures()) );
        vo.setLocation( po.getLocation() );
        vo.setLatitude( po.getLatitude() );
        vo.setLongitude( po.getLongitude() );
        vo.setVoice( po.getVoice() );
        vo.setStatus( po.getStatus() );
        vo.setStatusLabel(ApproveStatusEnum.getLabelByStatus( po.getStatus() ));
        vo.setAuditReason( po.getAuditReason() );
        vo.setAuditTime( po.getAuditTime() );
        vo.setCreateTime( po.getCreateTime() );
        vo.setLikeCount(Objects.isNull(po.getLikeCount()) ? 0 : po.getLikeCount() );
        vo.setCommentCount(Objects.isNull(po.getCommentCount()) ? 0 : po.getCommentCount() );
        return vo;
    }
}