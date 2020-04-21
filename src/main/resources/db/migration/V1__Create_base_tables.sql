DROP TABLE IF EXISTS `user`;
CREATE TABLE user  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `account_id` varchar(100) NOT NULL,
  `login_name` varchar(50),
  `token` char(36) NOT NULL,
  `gmt_create` bigint(20) NULL DEFAULT NULL,
  `gmt_modified` bigint(20) NULL DEFAULT NULL,
  `avatar_url` varchar(200) NULL DEFAULT NULL,
  `bio` varchar(256) NULL DEFAULT NULL,
  `notification_count` int(10) NULL DEFAULT 0,
  `pwd` varchar(30) NULL DEFAULT NULL
);

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `parent_id` bigint(20) NOT NULL COMMENT '父类id',
  `type` int(1) NOT NULL COMMENT '1：问题的评论  2：回复的评论',
  `commentator` bigint(20) NOT NULL COMMENT '评论人id',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gem_modified` bigint(20) NOT NULL COMMENT '更新时间',
  `like_count` bigint(20) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `content` varchar(1024) NOT NULL,
  `comment_count` int(10) NOT NULL DEFAULT 0
);
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `notifier` bigint(20) NOT NULL COMMENT '发出通知的人',
  `receiver` bigint(20) NOT NULL COMMENT '接收通知的人',
  `outerId` bigint(20) NULL DEFAULT NULL COMMENT '被回复的问题或评论的id',
  `type` int(1) NULL DEFAULT NULL COMMENT '区分评论或者回复，1代表回复问题，2代表回复评论，3代表点赞问题，4代表点赞评论',
  `gmt_create` bigint(20) NULL DEFAULT NULL,
  `status` int(1) NULL DEFAULT 0 COMMENT '0代表未读，1代表以读'
);

DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `title` varchar(50) NULL DEFAULT NULL,
  `description` text NULL,
  `gmt_create` bigint(20) NULL DEFAULT NULL,
  `gmt_modified` bigint(20) NULL DEFAULT NULL,
  `creator` bigint(20) NULL DEFAULT NULL,
  `comment_count` bigint(20) NULL DEFAULT 0,
  `view_count` bigint(20) NULL DEFAULT 0,
  `like_count` bigint(20) NULL DEFAULT 0,
  `tag` varchar(256) NULL DEFAULT NULL
);

DROP TABLE IF EXISTS `verify`;
CREATE TABLE `verify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `account_id` varchar(50) NOT NULL COMMENT '邮箱号',
  `verify_code` int(10) NOT NULL
);
