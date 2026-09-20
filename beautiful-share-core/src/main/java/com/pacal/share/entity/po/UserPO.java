package com.pacal.share.entity.po;

import lombok.Data;

/**
 * 用户对象(对应 stack_user 表)
 */
@Data
public class UserPO {
    private Integer id;
    /** 用户名(微信昵称/默认昵称) */
    private String username;
    /** 头像 */
    private String avatar;
    /** 微信 unionId */
    private String wxUnionId;
    /** 微信 openId */
    private String wxOpenId;
    /** 角色id: 1-超级管理员 2-管理员 3-普通用户 */
    private Integer roleId;
    /** 性别: 0-未知 1-男 2-女 */
    private Integer gender;
    private String description;
    /** 用户状态: A-正常 C-禁用 */
    private String status;
    /** 最后登录时间 */
    private String lastLoginTime;
    private String createTime;
    private String updateTime;
}