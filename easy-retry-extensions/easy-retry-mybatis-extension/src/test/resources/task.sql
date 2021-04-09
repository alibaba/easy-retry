CREATE TABLE IF NOT EXISTS easy_retry_task
(
    id
    bigint
    unsigned
    NOT
    NULL
    AUTO_INCREMENT
    COMMENT
    '主键'
    PRIMARY
    KEY,
    gmt_create
    datetime
    NOT
    NULL
    COMMENT
    '创建时间',
    gmt_modified
    datetime
    NOT
    NULL
    COMMENT
    '修改时间',
    sharding
    varchar
(
    64
) NULL COMMENT '1',
    biz_id varchar
(
    64
) NULL COMMENT '2',
    executor_name varchar
(
    512
) NULL COMMENT '3',
    executor_method_name varchar
(
    512
) NULL COMMENT '4',
    retry_status tinyint NULL COMMENT '5',
    args_str varchar
(
    3000
) NULL COMMENT '6',
    ext_attrs varchar
(
    3000
) NULL COMMENT '7'
    );