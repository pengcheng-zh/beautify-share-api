-- =============================================================
-- beautify-share 数据库初始化脚本
-- 适用 MySQL 8.x (utf8mb4)
-- 数据库: beautify_share
-- 表:
--   stack_user            用户
--   user_post             用户发表的文章
--   user_post_comment     文章评论
--   user_post_like        文章点赞
--   user_post_favorite    文章收藏
-- =============================================================

CREATE DATABASE IF NOT EXISTS `beautify_share`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `beautify_share`;

-- -------------------------------------------------------------
-- 用户表
-- -------------------------------------------------------------
DROP TABLE IF EXISTS `stack_user`;
CREATE TABLE `stack_user` (
  `id`              INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username`        VARCHAR(64)  NOT NULL DEFAULT ''            COMMENT '用户昵称/用户名',
  `avatar`          VARCHAR(512) NOT NULL DEFAULT ''            COMMENT '头像URL',
  `phone`           VARCHAR(32)  NOT NULL DEFAULT ''            COMMENT '手机号',
  `wx_union_id`     VARCHAR(64)  NOT NULL DEFAULT ''            COMMENT '微信unionId',
  `wx_open_id`      VARCHAR(64)  NOT NULL DEFAULT ''            COMMENT '微信openId',
  `role_id`         INT          NOT NULL DEFAULT 3              COMMENT '角色: 1-超管 2-管理员 3-普通用户',
  `gender`          INT          NOT NULL DEFAULT 0              COMMENT '性别: 0-未知 1-男 2-女',
  `status`          VARCHAR(8)   NOT NULL DEFAULT 'A'           COMMENT '状态: A-正常 C-禁用',
  `session_key`     VARCHAR(64)  NOT NULL DEFAULT ''            COMMENT '微信session_key',
  `last_login_time` VARCHAR(32)  NOT NULL DEFAULT ''            COMMENT '最后登录时间',
  `create_time`     VARCHAR(32)  NOT NULL DEFAULT ''            COMMENT '创建时间',
  `update_time`     VARCHAR(32)  NOT NULL DEFAULT ''            COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_wx_open_id` (`wx_open_id`),
  KEY `idx_role_id`   (`role_id`),
  KEY `idx_status`    (`status`),
  KEY `idx_username`  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- -------------------------------------------------------------
-- 文章表
-- -------------------------------------------------------------
DROP TABLE IF EXISTS `user_post`;
CREATE TABLE `user_post` (
  `id`              INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id`         INT          NOT NULL                      COMMENT '作者用户id',
  `content`         TEXT         NOT NULL                      COMMENT '文章内容',
  `pictures`        TEXT         NULL                          COMMENT '图片URL列表,逗号分隔',
  `location`        VARCHAR(128) NOT NULL DEFAULT ''           COMMENT '位置描述',
  `latitude`        VARCHAR(32)  NOT NULL DEFAULT ''           COMMENT '纬度',
  `longitude`       VARCHAR(32)  NOT NULL DEFAULT ''           COMMENT '经度',
  `voice`           VARCHAR(512) NOT NULL DEFAULT ''           COMMENT '语音URL',
  `status`          VARCHAR(8)   NOT NULL DEFAULT 'A'          COMMENT '审核状态: A-待审核 P-通过 R-拒绝',
  `audit_user_id`   INT          NULL                          COMMENT '审核人',
  `audit_reason`    VARCHAR(512) NOT NULL DEFAULT ''           COMMENT '审核备注',
  `audit_time`      VARCHAR(32)  NOT NULL DEFAULT ''           COMMENT '审核时间',
  `create_time`     VARCHAR(32)  NOT NULL DEFAULT ''           COMMENT '创建时间',
  `update_time`     VARCHAR(32)  NOT NULL DEFAULT ''           COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id`       (`user_id`),
  KEY `idx_status`        (`status`),
  KEY `idx_create_time`   (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户文章';

-- -------------------------------------------------------------
-- 评论表
-- -------------------------------------------------------------
DROP TABLE IF EXISTS `user_post_comment`;
CREATE TABLE `user_post_comment` (
  `id`              INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id`         INT          NOT NULL                      COMMENT '评论用户id',
  `post_id`         INT          NOT NULL                      COMMENT '所属文章id',
  `content`         VARCHAR(1024) NOT NULL DEFAULT ''          COMMENT '评论内容',
  `status`          VARCHAR(8)   NOT NULL DEFAULT 'A'          COMMENT '审核状态: A-待审核 P-通过 R-拒绝',
  `audit_user_id`   INT          NULL                          COMMENT '审核人',
  `audit_reason`    VARCHAR(512) NOT NULL DEFAULT ''           COMMENT '审核备注',
  `audit_time`      VARCHAR(32)  NOT NULL DEFAULT ''           COMMENT '审核时间',
  `create_time`     VARCHAR(32)  NOT NULL DEFAULT ''           COMMENT '创建时间',
  `update_time`     VARCHAR(32)  NOT NULL DEFAULT ''           COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_post_id`       (`post_id`),
  KEY `idx_user_id`       (`user_id`),
  KEY `idx_status`        (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章评论';

-- -------------------------------------------------------------
-- 文章点赞表
-- -------------------------------------------------------------
DROP TABLE IF EXISTS `user_post_like`;
CREATE TABLE `user_post_like` (
  `id`          INT         NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id`     INT         NOT NULL                      COMMENT '点赞用户id',
  `post_id`     INT         NOT NULL                      COMMENT '被点赞的文章id',
  `create_time` VARCHAR(32) NOT NULL DEFAULT ''           COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_post` (`user_id`, `post_id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章点赞';

-- -------------------------------------------------------------
-- 文章收藏表
-- -------------------------------------------------------------
DROP TABLE IF EXISTS `user_post_favorite`;
CREATE TABLE `user_post_favorite` (
  `id`          INT         NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id`     INT         NOT NULL                      COMMENT '收藏用户id',
  `post_id`     INT         NOT NULL                      COMMENT '被收藏的文章id',
  `create_time` VARCHAR(32) NOT NULL DEFAULT ''           COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_post` (`user_id`, `post_id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章收藏';

-- -------------------------------------------------------------
-- 初始数据: 超级管理员 (username=admin / password=admin123)
-- 若开启 bootstrap.admin,启动时如未发现 role_id=1 用户也会自动创建
-- 这里手动插入便于初始化即可登录后台
-- -------------------------------------------------------------
INSERT INTO `stack_user` (`username`, `avatar`, `role_id`, `gender`, `status`, `create_time`, `last_login_time`)
VALUES ('admin', '', 1, 0, 'A', DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s'));