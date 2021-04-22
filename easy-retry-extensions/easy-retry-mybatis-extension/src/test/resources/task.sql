CREATE TABLE IF NOT EXISTS easy_retry_task (
    id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键' PRIMARY KEY,
    gmt_create datetime NOT NULL COMMENT '创建时间',
    gmt_modified datetime NOT NULL COMMENT '修改时间',
    sharding varchar(64) NULL COMMENT '数据库分片字段',
    biz_id varchar(64) NULL COMMENT '业务id',
    executor_name varchar(512) NULL COMMENT '执行名称',
    executor_method_name varchar(512) NULL COMMENT '执行方法名称',
    retry_status tinyint NOT NULL COMMENT '重试状态',
    args_str varchar(3000) NULL COMMENT '执行方法参数',
    ext_attrs varchar(3000) NULL COMMENT '扩展字段'
);