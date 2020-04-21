
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `parent_id` bigint(0) NOT NULL COMMENT '父类id',
  `type` int(0) NOT NULL COMMENT '父类类型',
  `Commentator` bigint(0) NULL DEFAULT NULL COMMENT '评论人id',
  `gmt_create` bigint(0) NOT NULL COMMENT '创建时间',
  `gem_modified` bigint(0) NOT NULL COMMENT '更新时间',
  `like_count` bigint(0) NULL DEFAULT 0 COMMENT '点赞数',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `comment_count` int(0) NULL DEFAULT 0,
)
