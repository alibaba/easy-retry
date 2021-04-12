CREATE TABLE IF NOT EXISTS easy_retry_task (
    id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键' PRIMARY KEY,
    gmt_create datetime NOT NULL COMMENT '创建时间',
    gmt_modified datetime NOT NULL COMMENT '修改时间',
    sharding varchar(64) NULL COMMENT '1',
    biz_id varchar(64) NULL COMMENT 'biz_id',
    executor_name varchar(512) NULL COMMENT 'executor_name',
    executor_method_name varchar(512) NULL COMMENT 'executor_method_name',
    retry_status tinyint NOT NULL COMMENT 'retry_status',
    args_str varchar(3000) NULL COMMENT 'args_str',
    ext_attrs varchar(3000) NULL COMMENT 'ext_attrs'
);