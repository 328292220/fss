CREATE TABLE `fss_role`
(
    `role_id`     bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色名字',
    `ename`       varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色名字',
    `description` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
    `enabled_flag`          char(1)    NOT NULL DEFAULT 'Y' COMMENT '启用标识',
    `creation_date`         datetime   NOT NULL DEFAULT now()  COMMENT '创建时间',
    `created_by`            int(11)    NOT NULL COMMENT '创建人',
    `last_update_date`      datetime   NOT NULL DEFAULT now()  COMMENT '最后更新时间',
    `last_updated_by`       int(11)    NOT NULL COMMENT '最后更新人',
    `is_deleted`          int(1)              DEFAULT '0' COMMENT '是否未删除',
    PRIMARY KEY (`role_id`),
    UNIQUE KEY `fss_role_U1` (`ename`, `is_deleted`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;