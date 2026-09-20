package com.pacal.share.entity.request;

import lombok.Data;

/**
 * 通用审核请求
 */
@Data
public class AuditRequest {
    /** 待审核项 id */
    private Integer id;
    /** 状态: P-通过 R-拒绝 */
    private String status;
    /** 备注/原因 */
    private String reason;
}