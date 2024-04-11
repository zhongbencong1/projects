-- 创建 repository_goods 数据表
CREATE TABLE IF NOT EXISTS `project_repository`.`repository_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `goods_category` varchar(64) NOT NULL DEFAULT '' COMMENT '商品类别',
  `brand_category` varchar(64) NOT NULL DEFAULT '' COMMENT '品牌分类',
  `goods_name` varchar(64) NOT NULL DEFAULT '' COMMENT '商品名称',
  `goods_pic` varchar(256) NOT NULL DEFAULT '' COMMENT '商品图片',
  `goods_description` varchar(512) NOT NULL DEFAULT '' COMMENT '商品描述信息',
  `goods_status` int(11) NOT NULL DEFAULT 0 COMMENT '商品状态',
  `price` int(11) NOT NULL DEFAULT 0 COMMENT '商品价格',
  `supply` bigint(20) NOT NULL DEFAULT 0 COMMENT '总供应量',
  `inventory` bigint(20) NOT NULL DEFAULT 0 COMMENT '库存',
  `goods_property` varchar(1024) NOT NULL DEFAULT '' COMMENT '商品属性',
  `create_time` BIGINT COMMENT '创建时间',
  `update_time` BIGINT COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `goods_category_brand_name` (`goods_category`, `brand_category`, `goods_name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='商品表';
