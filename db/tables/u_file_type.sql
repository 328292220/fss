create table fss_file_type
(
    type_id              bigint not null AUTO_INCREMENT comment '类型编号',
    name            varchar(100) comment '类型名称',
    status          varchar(2) comment '类型状态',
    primary key (type_id)
);

alter table fss_file_type comment '文件类型';