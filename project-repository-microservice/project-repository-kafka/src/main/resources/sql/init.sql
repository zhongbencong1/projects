SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for repository_user
-- ----------------------------
DROP TABLE IF EXISTS `repository_user`;
CREATE TABLE `repository_user`  (
    `id`        bigint      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `username`  varchar(64) NOT NULL DEFAULT ''     COMMENT '用户名',
    PRIMARY     KEY (`id`)  USING BTREE,
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;