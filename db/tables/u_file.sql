create table fss_file
(
    file_id                 bigint       not null AUTO_INCREMENT comment '文件编号',
    type_id                 bigint comment '类型编号',
    dir_id                  bigint comment '文件夹编号',
    user_id                 bigint comment '用户编号',
    file_name               varchar(512) not null comment '文件名',
    file_size               int(11)      not null comment '文件大小(单位kb)',
    `creation_date`         datetime     NOT NULL DEFAULT now() COMMENT '创建时间',
    `created_by`            bigint       NOT NULL COMMENT '创建人',
    `last_update_date`      datetime     NOT NULL DEFAULT now() COMMENT '最后更新时间',
    `last_updated_by`       bigint       NOT NULL COMMENT '最后更新人',
    `is_deleted`            int(1)                DEFAULT '0' COMMENT '是否未删除',
    `object_version_number` bigint(1)    NOT NULL DEFAULT '0' COMMENT '版本号',
    primary key (file_id),
    key file_dir_id (file_id, dir_id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

alter table fss_file
    comment '文件信息';