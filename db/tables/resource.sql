CREATE TABLE `fss_resource`
(
    `resource_id`           bigint(20)   NOT NULL AUTO_INCREMENT,
    `resource_url`          varchar(50)  NOT NULL COMMENT '资源URL',
    `resource_name`         varchar(150) NOT NULL COMMENT '资源名称',
    `resource_desc`         varchar(240)          DEFAULT NULL COMMENT '资源说明',
    `menu_id`               bigint(20)            DEFAULT NULL COMMENT '菜单ID',
    `enabled_flag`          char(1)      NOT NULL DEFAULT 'Y' COMMENT '启用标识',
    `creation_date`         datetime     NOT NULL DEFAULT now()  COMMENT '创建时间',
    `created_by`            int(11)      NOT NULL COMMENT '创建人',
    `last_update_date`      datetime     NOT NULL DEFAULT now()  COMMENT '最后更新时间',
    `last_updated_by`       int(11)      NOT NULL COMMENT '最后更新人',
    `is_deleted`          int(1)                DEFAULT '0' COMMENT '是否未删除',
    PRIMARY KEY (`resource_id`),
    UNIQUE KEY `fss_resource_U1` (`resource_url`, `is_deleted`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='资源表';