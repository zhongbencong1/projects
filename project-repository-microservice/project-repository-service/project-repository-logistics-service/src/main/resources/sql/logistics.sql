-- 物流信息表
create table if not exists `project_repository`.`repository_logistics` (
    `id` bigint(20) not null auto_increment comment '自增主键',
    `user_id` bigint(20) not null default 0 comment '用户id',
    `order_id` bigint(20) not null default 0 comment '订单id',
    `address_id` bigint(20) not null default 0 comment '用户地址id',
    `extra_info` varchar(512) not null comment '备注信息(json)',
    `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '更新时间',
    primary key (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='物流信息表';