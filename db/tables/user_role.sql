CREATE TABLE `fss_user_role`
(
    `id`      bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint(11) DEFAULT NULL COMMENT '用户主键',
    `role_id` bigint(11) DEFAULT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `fss_user_role_U1` (`user_id`, `role_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;