create table fss_dir
(
    dir_id                  bigint        not null AUTO_INCREMENT comment '文件夹编号',
    name                    varchar(512)  not null comment '文件夹名称',
    parent_id               bigint                 default NULL comment '父文件夹',
    user_id                 bigint        not null comment '创建人',
    path                    varchar(1024) not null comment '文件夹地址',
    `creation_date`         datetime      NOT NULL DEFAULT now() COMMENT '创建时间',
    `created_by`            bigint        NOT NULL COMMENT '创建人',
    `last_update_date`      datetime      NOT NULL DEFAULT now() COMMENT '最后更新时间',
    `last_updated_by`       bigint        NOT NULL COMMENT '最后更新人',
    `is_deleted`            int(1)                 DEFAULT '0' COMMENT '是否未删除',
    `object_version_number` bigint(1)     NOT NULL DEFAULT '0' COMMENT '版本号',
    primary key (dir_id),
    key dir_user_id (user_id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

alter table fss_dir
    comment '文件夹';