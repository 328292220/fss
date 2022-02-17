CREATE TABLE `fss_role_resource`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT,
    `role_id`               bigint(20) NOT NULL COMMENT '角色ID',
    `resource_id`           bigint(20) NOT NULL COMMENT '资源ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `fss_role_resource_U1` (`role_id`, `resource_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色资源分配表';