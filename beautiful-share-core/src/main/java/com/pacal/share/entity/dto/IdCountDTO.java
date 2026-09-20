package com.pacal.share.entity.dto;

import lombok.Data;

/**
 * 通用统计结果: 关联id + 数量 (用于按 postId 批量统计点赞/收藏/评论数)
 */
@Data
public class IdCountDTO {
    /** 关联的业务 id (如 postId) */
    private Integer id;
    /** 数量 */
    private Integer count;
}
