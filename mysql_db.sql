CREATE TABLE `so_account` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`cust_id` BIGINT(20) NOT NULL,
	`account` VARCHAR(50) NOT NULL,
	`password` VARCHAR(50) NOT NULL,
	`role` SMALLINT(6) NOT NULL DEFAULT '40' COMMENT '10.Admin,20.Operator,30.User,40.Unknown',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `unique_account` (`account`),
	INDEX `cust_id` (`cust_id`),
	INDEX `index_account` (`account`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `so_customer` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'PKEY 10001开始',
	`cust_name` VARCHAR(150) NOT NULL COMMENT '公司名称',
	`type` SMALLINT(6) NOT NULL DEFAULT '2' COMMENT '公司类型(1.本公司,2.其他)',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=10001
;


CREATE TABLE `so_devices` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'PKEY',
	`dev_no` VARCHAR(50) NOT NULL DEFAULT '00000000' COMMENT '设备号',
	`cust_id` BIGINT(20) NOT NULL DEFAULT '1',
	`gps_info` VARCHAR(100) NULL DEFAULT NULL,
	`ip_addr` VARCHAR(100) NULL DEFAULT NULL,
	`data_server_ip` VARCHAR(100) NULL DEFAULT NULL,
	`data_server_port` INT(11) NULL DEFAULT NULL,
	`create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `unique_dev_no` (`dev_no`),
	INDEX `index_dev_no` (`dev_no`),
	INDEX `cust_id` (`cust_id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `so_running_data` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'KEY',
	`dev_id` INT(11) NULL DEFAULT '0' COMMENT '设备ID',
	`real_time` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '客户端调用时间',
	`warnning_code` INT(11) NOT NULL DEFAULT '0' COMMENT '警告码(0:正常)',
	`ip_addr` VARCHAR(40) NOT NULL DEFAULT '0' COMMENT 'IP地址',
	`charging_voltage` FLOAT NOT NULL DEFAULT '0' COMMENT '太阳能充电电压',
	`charging_current` FLOAT NOT NULL DEFAULT '0' COMMENT '太阳能充电电流',
	`dc_charging_voltage` FLOAT NOT NULL DEFAULT '0' COMMENT 'DC充电电压',
	`dc_charging_current` FLOAT NOT NULL DEFAULT '0' COMMENT 'DC充电电流',
	`battery_voltage` FLOAT NOT NULL DEFAULT '0' COMMENT '电池电压',
	`battery_charging_current` FLOAT NOT NULL DEFAULT '0' COMMENT '电池充电电流',
	`rt_ntc_temperature` FLOAT NOT NULL DEFAULT '0' COMMENT 'NTC实时温度值',
	`load_current_1` FLOAT NOT NULL DEFAULT '0' COMMENT '负载1工作电流',
	`load_current_2` FLOAT NOT NULL DEFAULT '0' COMMENT '负载2工作电流',
	`load_current_3` FLOAT NOT NULL DEFAULT '0' COMMENT '负载3工作电流',
	`load_current_4` FLOAT NOT NULL DEFAULT '0' COMMENT '负载4工作电流',
	`gps_info` VARCHAR(100) NOT NULL DEFAULT '0' COMMENT 'GPS信息',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	PRIMARY KEY (`id`),
	INDEX `real_time` (`real_time`),
	INDEX `warnning_code` (`warnning_code`),
	INDEX `create_time` (`create_time`)
)
COMMENT='太阳能控制板运行数据'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `so_working_mode` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`cust_id` BIGINT(20) NOT NULL DEFAULT '0',
	`dev_id` VARCHAR(100) NOT NULL DEFAULT '0',
	`continuous` BIT(1) NOT NULL DEFAULT b'0' COMMENT '连续工作模式',
	`timing` BIT(1) NOT NULL DEFAULT b'1' COMMENT '定时开关工作模式',
	`point_of_time` TIME NULL DEFAULT NULL COMMENT '时间点',
	`fixed_timing_length` BIT(1) NOT NULL DEFAULT b'1' COMMENT '定时长开关工作模式',
	`time_interval` MEDIUMINT(9) NULL DEFAULT NULL COMMENT '时长间隔',
	`emergency` BIT(1) NOT NULL DEFAULT b'1' COMMENT '应急开关工作模式',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`create_by` BIGINT(20) NULL DEFAULT NULL COMMENT '创建人',
	PRIMARY KEY (`id`),
	INDEX `create_time` (`create_time`)
)
COMMENT='工作模式配置'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=17
;


CREATE TABLE `so_app_version` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`ver_no` INT(11) NOT NULL,
	`type` SMALLINT(6) NOT NULL COMMENT '客户端类型 10:apk, 50:dmg,100:mc',
	`info` VARCHAR(256) NULL DEFAULT NULL,
	`path` VARCHAR(512) NOT NULL,
	`file_name` VARCHAR(150) NOT NULL,
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
)
COMMENT='更新信息表'
ENGINE=InnoDB
;

