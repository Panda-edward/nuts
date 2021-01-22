CREATE TABLE `nuts_retry_record` (
  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '自增id',
  `biz_type` varchar(16) NOT NULL DEFAULT '' COMMENT '业务类型' ,
  `biz_no` varchar(64) NOT NULL DEFAULT '' COMMENT '业务唯一主键' ,
  `params` varchar(1000) NOT NULL DEFAULT '' COMMENT '调用方法参数(json)',
  `retry_status` TINYINT NOT NULL DEFAULT 0 COMMENT '重试状态',
  `retry_count` INT NOT NULL DEFAULT 0 COMMENT '重试次数',
  `next_retry_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '下次重试时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  unique KEY `uk_biz_no_type` (`biz_no`,`biz_type`),
  KEY `idx_next_time` (`next_retry_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1