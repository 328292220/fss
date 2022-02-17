drop table if exists fss_menu;
CREATE TABLE `fss_menu`
(
    `menu_id`               bigint(20)   NOT NULL AUTO_INCREMENT,
    `menu_code`             varchar(50)  NOT NULL COMMENT '菜单编码',
    `menu_name`             varchar(150) NOT NULL COMMENT '菜单名称',
    `menu_desc`             varchar(240)          DEFAULT NULL COMMENT '菜单说明',
    `path`                  varchar(50)           DEFAULT NULL COMMENT '跳转路径',
    `parent_menu_id`        bigint(20)            DEFAULT NULL COMMENT '上级菜单ID',
    `icon`                  varchar(100)          DEFAULT NULL COMMENT '前端图标',
    `order_by`              int(11)               DEFAULT '0' COMMENT '排序',
    `enabled_flag`          char(1)      NOT NULL DEFAULT 'Y' COMMENT '启用标识',
    `creation_date`         datetime     NOT NULL DEFAULT now() COMMENT '创建时间',
    `created_by`            int(11)      NOT NULL COMMENT '创建人',
    `last_update_date`      datetime     NOT NULL DEFAULT now() COMMENT '最后更新时间',
    `last_updated_by`       int(11)      NOT NULL COMMENT '最后更新人',
    `is_deleted`          int(1)                DEFAULT '0' COMMENT '是否未删除',
    PRIMARY KEY (`menu_id`),
    UNIQUE KEY `fss_menu_U1` (`menu_code`, `is_deleted`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单表';