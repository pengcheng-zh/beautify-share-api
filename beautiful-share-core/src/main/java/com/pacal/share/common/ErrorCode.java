package com.pacal.share.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS("10000", "success"),
    FAIL("10001", "failed"),
    CONNECT_TIME_OUT("10002", "连接超时"),
    HTTP_CONNECT_EXCEPTION("10003", "无响应"),
    SQL_EXCEPTION("10004", "数据库错误"),

    /** token 相关 */
    TOKEN_IS_INVALID("10005", "你还未登录"),
    NO_LOGIN("10006", "未登录"),
    TOKEN_EXPIRED("10007", "验证已过期"),
    AUTH_TOKEN_FAIL("10008", "验证失败"),

    MAX_FILE_SIZE("10009", "上传文件大小超过限制"),

    /** 用户相关 */
    USER_NOT_EXIST("10100", "用户不存在"),
    USER_EXIST("10101", "用户已存在"),
    USER_DISABLED("10102", "用户已被禁用,请联系管理员"),
    USERNAME_EMPTY("10103", "请给自己取一个用户名"),
    USERNAME_INVALID("10104", "用户名太长或太短"),
    AVATAR_EMPTY("10105", "头像不能为空"),

    /** 文件上传 */
    FILE_UPLOAD_EMPTY("10120", "请选择上传文件"),
    FILE_UPLOAD_ERROR("10121", "文件上传错误"),
    RESOURCE_EMPTY("10130", "上传文件不能为空"),
    RESOURCE_ERROR_TYPE("10131", "文件类型不支持"),
    RESOURCE_UPLOAD_FAIL("10132", "文件上传失败"),

    /** 微信 */
    WX_ACCESS_CODE_EMPTY("11000", "微信登录验证code为空"),
    WX_ACCESS_CODE_INVALID("11001", "微信登录验证code不正确"),

    /** 文章(帖子)相关 */
    POST_NOT_EXIST("20001", "文章不存在"),
    POST_CONTENT_EMPTY("20002", "文章内容不能为空"),
    POST_CONTENT_TOO_LONG("20003", "文章内容过长(最多5000字)"),
    POST_PICTURES_TOO_MANY("20004", "最多上传9张图片"),
    POST_LOCATION_TOO_LONG("20005", "位置描述过长"),
    POST_VOICE_TOO_LONG("20006", "语音URL过长"),
    POST_STATUS_INVALID("20007", "文章状态不正确"),
    POST_NOT_OWNER("20008", "只能操作自己的文章"),

    /** 评论相关 */
    COMMENT_NOT_EXIST("21001", "评论不存在"),
    COMMENT_CONTENT_EMPTY("21002", "评论内容不能为空"),
    COMMENT_CONTENT_TOO_LONG("21003", "评论内容过长(最多500字)"),
    COMMENT_POST_NOT_EXIST("21004", "评论的文章不存在"),
    COMMENT_NOT_OWNER("21005", "只能操作自己的评论"),

    /** 审核 */
    APPROVE_STATUS_INVALID("30001", "不是有效的审核状态"),
    POST_REJECT_REASON("30002", "需要写明审核不通过的原因"),

    /** 通用 */
    PARAMS_ERROR("99996", "参数错误"),
    NO_RIGHT("99995", "没有权限"),
    REPEAT_SUBMIT("99991", "你请求的太快了"),
    MESSAGE_INVALID("99992", "非法伪造"),
    SECRET_KEY_INVALID("99990", "用户权限校验失败"),
    COMMON_ERROR("99999", "%s"),
    SYSTEM_ERROR("99999", "系统错误");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}