CREATE TABLE `fss_user`
(
    `user_id`      bigint(11)                       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_name`    varchar(64) COLLATE utf8mb4_bin  NOT NULL COMMENT '用户名',
    `password`     varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
    `nick_name`    varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
    `gender`       int(2)                           DEFAULT NULL COMMENT '性别（1男 2女）',
    `email`        varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
    `enabled_flag`          char(1)    NOT NULL DEFAULT 'Y' COMMENT '启用标识',
    `creation_date`         datetime   NOT NULL DEFAULT now()  COMMENT '创建时间',
    `created_by`            int(11)    NOT NULL COMMENT '创建人',
    `last_update_date`      datetime   NOT NULL DEFAULT now()  COMMENT '最后更新时间',
    `last_updated_by`       int(11)    NOT NULL COMMENT '最后更新人',
    `is_deleted`          int(1)              DEFAULT '0' COMMENT '是否未删除',
    `rsa_password` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT 'RSA密码',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `fss_user_U1` (`user_name`, `is_deleted`)

) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;