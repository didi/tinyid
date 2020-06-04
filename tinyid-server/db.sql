CREATE TABLE `tiny_id_info`
(
  `id`          BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `biz_type`    VARCHAR(63)         NOT NULL DEFAULT '' COMMENT '业务类型，唯一',
  `begin_id`    BIGINT(20)          NOT NULL DEFAULT '0' COMMENT '开始id，仅记录初始值，无其他含义。初始化时begin_id和max_id应相同',
  `max_id`      BIGINT(20)          NOT NULL DEFAULT '0' COMMENT '当前最大id',
  `step`        INT(11)             NULL     DEFAULT '0' COMMENT '号段步长',
  `delta`       INT(11)             NOT NULL DEFAULT '1' COMMENT '每次id增量',
  `remainder`   INT(11)             NOT NULL DEFAULT '0' COMMENT '余数',
  `remark`      VARCHAR(50)         NOT NULL DEFAULT '' COMMENT '备注',
  `update_time` TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version`     BIGINT(20)          NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uniq_biz_type` (`biz_type`)
)
  COMMENT ='id信息表'
  COLLATE = 'utf8_general_ci'
  ENGINE = InnoDB
  AUTO_INCREMENT = 1;

CREATE TABLE `tiny_id_token`
(
  `id`          INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `token`       VARCHAR(255)     NOT NULL DEFAULT '' COMMENT 'token',
  `biz_type`    VARCHAR(63)      NOT NULL DEFAULT '' COMMENT '此token可访问的业务类型标识',
  `remark`      VARCHAR(255)     NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
)
  COMMENT ='token信息表'
  COLLATE = 'utf8_general_ci'
  ENGINE = InnoDB
  AUTO_INCREMENT = 1;

