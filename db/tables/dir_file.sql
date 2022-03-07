drop table if exists browse_record;

drop table if exists dir_inf;

drop table if exists download_record;

drop table if exists file_cate;

drop table if exists file_inf;

drop table if exists user_inf;

/*==============================================================*/
/* Table: browse_record                                         */
/*==============================================================*/
create table browse_record
(
    browse_id            varchar(64) not null comment '浏览编号',
    file_id              varchar(64) comment '文件编号',
    user_id              varchar(64) comment '用户编号',
    browse_time          varchar(64) comment '浏览时间',
    primary key (browse_id)
);

alter table browse_record comment '文件浏览记录';

/*==============================================================*/
/* Table: dir_inf                                               */
/*==============================================================*/
create table fss_dir
(
    dir_id               varchar(64) not null comment '文件夹编号',
    dir_name             varchar(512) not null comment '文件夹名称',
    parent_dir           varchar(64) default NULL comment '父文件夹',
    dir_user_id          varchar(64) not null comment '创建人',
    dir_path             varchar(1024) not null comment '文件夹地址',
    primary key (dir_id),
    key dir_user_id (dir_user_id)
)
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table fss_dir comment '文件夹';

/*==============================================================*/
/* Table: download_record                                       */
/*==============================================================*/
create table download_record
(
    download_id          varchar(64) not null comment '下载编号',
    file_id              varchar(64) comment '文件编号',
    user_id              varchar(64) comment '用户编号',
    download_time        varchar(64) comment '浏览时间',
    primary key (download_id)
);

alter table download_record comment '文件下载记录';

/*==============================================================*/
/* Table: file_cate                                             */
/*==============================================================*/
create table fss_file_type
(
    type_id              varchar(64) not null comment '类型编号',
    name            varchar(100) comment '类型名称',
    status          varchar(2) comment '类型状态',
    primary key (type_id)
);

alter table fss_file_type comment '文件类型';

/*==============================================================*/
/* Table: file_inf                                              */
/*==============================================================*/
create table fss_file
(
    file_id              varchar(64) not null comment '文件编号',
    type_id              varchar(64) comment '类型编号',
    dir_id               varchar(64) comment '文件夹编号',
    user_id              varchar(64) comment '用户编号',
    file_name            varchar(512) not null comment '文件名',
    file_size            int(11) not null comment '文件大小(单位kb)',
    file_upload_time     varchar(64) not null comment '上传时间',
    file_status          int(2) not null comment '文件状态',
    primary key (file_id),
    key file_dir_id (file_id,dir_id)
)
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table fss_file comment '文件信息';

/*==============================================================*/
/* Table: user_inf                                              */
/*==============================================================*/
create table user_inf
(
    user_id              varchar(64) not null comment '用户编号',
    username             varchar(50) not null comment '用户名',
    password             varchar(50) not null comment '密码',
    phone                varchar(100) not null comment '电话号码',
    register_time        varchar(64) not null comment '注册时间',
    status               int(2) not null comment '状态',
    primary key (user_id)
)
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table user_inf comment '用户信息';

alter table browse_record add constraint FK_Reference_4 foreign key (file_id)
    references file_inf (file_id) on delete restrict on update restrict;

alter table browse_record add constraint FK_Reference_5 foreign key (user_id)
    references user_inf (user_id) on delete restrict on update restrict;

alter table download_record add constraint FK_Reference_6 foreign key (file_id)
    references file_inf (file_id) on delete restrict on update restrict;

alter table download_record add constraint FK_Reference_7 foreign key (user_id)
    references user_inf (user_id) on delete restrict on update restrict;

alter table file_inf add constraint FK_Reference_1 foreign key (cate_id)
    references file_cate (cate_id) on delete restrict on update restrict;

alter table file_inf add constraint FK_Reference_2 foreign key (dir_id)
    references dir_inf (dir_id) on delete restrict on update restrict;

alter table file_inf add constraint FK_Reference_3 foreign key (user_id)
    references user_inf (user_id) on delete restrict on update restrict;

