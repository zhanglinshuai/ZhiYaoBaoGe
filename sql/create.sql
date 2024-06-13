-- auto-generated definition
-- auto-generated definition
create table user
(
    id            int auto_increment comment '用户id'
        primary key,
    user_age      int                                null comment '用户性别',
    user_gender   int                                null comment '用户性别',
    user_name     varchar(256)                       null comment '用户名',
    user_account  varchar(256)                       null comment '用户账号',
    user_password varchar(256)                       null comment '用户密码',
    user_role     int                                null comment '用户身份 0 - 普通用户 1- 管理员',
    user_phone    varchar(256)                       null comment '用户手机号',
    isDelete      int      default 0                 not null comment '是否删除 0-不删除  1- 删除',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null comment '更新时间'
)
    comment '用户表';

create table medicine
(
    id                     int auto_increment comment '药品id'
        primary key,
    medicine_name          varchar(256) null comment '药品名称',
    medicine_description   varchar(256) null comment '药品使用说明',
    medicine_price         int          null comment '药品价格',
    medicine_specification varchar(256) null comment '药品规格',
    medicine_manufacturer  varchar(256) null comment '药品生产厂家',
    medicine_inventory     varchar(256) null comment '药品库存量'
)
    comment '药品表';