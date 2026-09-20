package com.pacal.share.entity.request;

import lombok.Data;

/**
 * 微信小程序登录请求
 */
@Data
public class LoginRequest {
    /** 微信登录凭证 */
    private String code;
    /** 微信昵称 */
    private String nickName;
    /** 微信头像 */
    private String avatarUrl;
    /** 性别 */
    private Integer gender;
    /** 自定义用户名(可选) */
    private String username;
}